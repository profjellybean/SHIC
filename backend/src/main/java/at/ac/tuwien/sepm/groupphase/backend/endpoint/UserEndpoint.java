package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UsernameDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ComplexUserMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.EmailConfirmationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.EmailCooldownException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.PasswordValidationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UsernameTakenException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;


@RestController
@RequestMapping(path = "/api/v1/user")
public class UserEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;
    private final ComplexUserMapper userMapperImpl;
    private final UserMapper userMapper;

    @Autowired
    public UserEndpoint(UserService userService, ComplexUserMapper userMapperImpl, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userMapperImpl = userMapperImpl;
    }


    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        LOGGER.info("Endpoint: POST /user");
        try {
            if (userRegistrationDto.getEmail().contains("email.com")) { // Users in Testdata have an @email.com domain. (No email confirmation is sent)

                userService.createUserWithoutEmailVerification(userRegistrationDto);
            } else {
                userService.createUserWithEmailVerification(userRegistrationDto);
            }

        } catch (UsernameTakenException | PasswordValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }


    }

    @PermitAll
    @GetMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    public void confirmUser(@RequestParam(value = "tkn") String confirmationTokenEncrypted) {
        LOGGER.info("Endpoint: GET /user/confirm?tkn={}", confirmationTokenEncrypted);
        try {
            userService.confirmUser(confirmationTokenEncrypted);
        } catch (EmailConfirmationException e) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }


    }


    @PermitAll
    @PutMapping("/confirmation")
    @ResponseStatus(HttpStatus.OK)
    public void resendUserEmailConfirmation(@RequestBody UsernameDto usernameDto) {
        LOGGER.info("Endpoint: PUT /user/confirmation {}" + usernameDto.getUsername());


        try {
            userService.resendUserEmailConfirmation(usernameDto.getUsername());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EmailCooldownException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }


    }


    @PermitAll
    @GetMapping("/confirmation")
    @ResponseStatus(HttpStatus.OK)
    public boolean isUserConfirmed(Authentication authentication) {
        LOGGER.info("Endpoint: GET /user/confirmation");
        try {
            return userService.getConfirmationStatusByName(authentication.getName());
        } catch (EmailConfirmationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }


    }



    @PermitAll                   //TODO just for Tests
    @PatchMapping
    public void test(@RequestBody UserLoginDto userLoginDto) {

        LOGGER.info("Endpoint: Test /user");

    }

    @PermitAll
    @GetMapping
    public UserDto getUserByUsername(@Param("username") String username) {
        LOGGER.info("Endpoint: getUserByUsername({})", username);
        return this.userMapper.userToUserDto(this.userService.findApplicationUserByUsername(username));
    }
}
