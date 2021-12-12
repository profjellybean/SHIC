package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.PasswordTooShortException;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;


@RestController
@RequestMapping(path = "/user")
public class UserEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserEndpoint(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserLoginDto userLoginDto) {
        LOGGER.info("Endpoint: POST /user");
        try {
            userService.createUser(userLoginDto);

        } catch (UsernameTakenException | PasswordTooShortException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (Exception e) {
            ///TODO Exception ( auch NotfoundException)
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
