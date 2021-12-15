package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UsernameDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.EmailConfirmationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.EmailCooldownException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.PasswordValidationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UsernameTakenException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;


@RestController
@RequestMapping(path = "/api/v1/user")
public class UserEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;

    @Autowired
    public UserEndpoint(UserService userService){
        this.userService = userService;
    }


    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserRegistrationDto userRegistrationDto){
        LOGGER.info("Endpoint: POST /user");
        try {
            if(userRegistrationDto.getEmail().contains("email.com")){           // Users in Testdata have an @email.com domain. (No email confirmation is sent)
                userService.createUserWithoutEmailVerification(userRegistrationDto);
            }else{
                userService.createUserWithEmailVerification(userRegistrationDto);
            }


        }catch (UsernameTakenException | PasswordValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,e.getMessage());
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }


    }
    @PermitAll
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void confirmUser(@RequestParam(value="confirm") String confirmationToken_encrypted){
        LOGGER.info("Endpoint: GET /user?confirmation={}",confirmationToken_encrypted);
        try {
            userService.confirmUser(confirmationToken_encrypted);
        }catch (EmailConfirmationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }


    }


    @PermitAll
    @PutMapping("/confirmation")
    @ResponseStatus(HttpStatus.OK)
    public void resendUserEmailConfirmation(@RequestBody UsernameDto usernameDto){
        LOGGER.info("Endpoint: PUT /user/confirmation {}"+usernameDto.getUsername());


        try {
            userService.resendUserEmailConfirmation(usernameDto.getUsername());
        }catch (NotFoundException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }catch (EmailCooldownException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,e.getMessage());
        }


    }


    @PermitAll
    @GetMapping("/confirmation")
    @ResponseStatus(HttpStatus.OK)
    public boolean isUserConfirmed(Authentication authentication){
        LOGGER.info("Endpoint: GET /user/confirmation");
        try {
            return userService.getConfirmationStatusByName(authentication.getName());
        }catch (EmailConfirmationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }


    }

    @PermitAll                   //TODO just for Tests
    @PatchMapping
    public void test(@RequestBody UserLoginDto userLoginDto){

            LOGGER.info("Endpoint: Test /user");

    }

}
