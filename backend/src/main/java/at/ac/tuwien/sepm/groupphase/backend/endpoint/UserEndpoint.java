package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.PasswordTooShortException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UsernameTakenException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;
import java.util.Map;


@RestController
@RequestMapping(path = "/user")
public class UserEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;

    @Autowired
    public UserEndpoint(UserService userService){

        this.userService = userService;
    }


    @PermitAll
    @PostMapping
    public void createUser(@RequestBody UserLoginDto userLoginDto){
        LOGGER.info("Endpoint: POST /user");
        try {
          userService.createUser(userLoginDto);

        }catch (UsernameTakenException | PasswordTooShortException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,e.getMessage());
        }catch (Exception e) {
            ///TODO Exception ( auch NotfoundException)
        }


    }

    @Secured("ROLE_USER")
    @PutMapping
    public void editUser(){
        LOGGER.info("Endpoint: PUT /user");
    }

}
