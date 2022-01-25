package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EmailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ImageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UsernameDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ComplexUserMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.EmailConfirmationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.EmailCooldownException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.PasswordValidationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UsernameTakenException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;


@RestController
@RequestMapping(path = "/api/v1/user")
public class UserEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;
    private final ComplexUserMapper userMapperImpl;
    private final UserMapper userMapper;
    private final ComplexUserMapper complexUserMapper;
    private final JwtTokenizer jwtTokenizer;

    @Autowired
    public UserEndpoint(UserService userService, ComplexUserMapper userMapperImpl, UserMapper userMapper, ComplexUserMapper complexUserMapper, JwtTokenizer jwtTokenizer) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userMapperImpl = userMapperImpl;
        this.complexUserMapper = complexUserMapper;
        this.jwtTokenizer = jwtTokenizer;
    }


    @Secured("ROLE_USER")
    @PutMapping("/picture")
    @ResponseStatus(HttpStatus.OK)
    ImageDto uploadImage(@RequestParam("file") MultipartFile multipartImage, Authentication authentication) {
        try {
            userService.editPicture(multipartImage.getBytes(), authentication.getName());
            return new ImageDto(multipartImage.getBytes());
        } catch (NotFoundException | ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }


    @Secured("ROLE_USER")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public String editUsername(@RequestBody UsernameDto newUsernameDto, Authentication authentication) {

        try {
            userService.editUsername(newUsernameDto.getUsername(), authentication.getName());
            List<String> roles = new LinkedList<>();
            roles.add("ROLE_USER");
            return "{ \"token\":\"" + jwtTokenizer.getAuthToken(newUsernameDto.getUsername(), roles) + " \"}";
        } catch (UsernameTakenException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }


    }

    @Secured("ROLE_USER")
    @PutMapping("/email")
    @ResponseStatus(HttpStatus.OK)
    public Long changeEmail(@RequestBody EmailDto emailDto, Authentication authentication) {
        try {

            return userService.changeEmail(emailDto, authentication.getName());

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Secured("ROLE_USER")
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

    @Secured("ROLE_USER")
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

    @Secured("ROLE_USER")
    @GetMapping("/confirmNew")
    @ResponseStatus(HttpStatus.OK)
    public void confirmNewEmail(@RequestParam(value = "tkn") String confirmationTokenEncrypted) {
        LOGGER.info("Endpoint: GET /user/confirmNew?tkn={}", confirmationTokenEncrypted);
        try {
            userService.confirmNewEmail(confirmationTokenEncrypted);
        } catch (EmailConfirmationException e) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }


    }

    @Secured("ROLE_USER")
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

    @Secured("ROLE_USER")
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

    @Secured("ROLE_USER")
    @GetMapping
    public UserDto getUser(Authentication authentication) {
        try {
            if (authentication == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            LOGGER.info("Endpoint: getUserByUsername({})", authentication.getName());
            ApplicationUser applicationUser = this.userService.findApplicationUserByUsername(authentication.getName());
            return this.complexUserMapper.userToUserDto(applicationUser);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @Secured("ROLE_USER")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserById(Authentication authentication, @PathVariable Long id) {

        try {
            LOGGER.info("Endpoint: DELETE /user/" + id);
            this.userService.deleteUserById(id);

        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
