package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ComplexUserMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.GroupService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1/group")
public class UserGroupEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final GroupService groupService;
    private final UserMapper userMapper;
    private final UserService userService;
    private final ComplexUserMapper complexUserMapper;

    public UserGroupEndpoint(GroupService groupService, UserMapper userMapper, UserService userService, ComplexUserMapper complexUserMapper) {
        this.groupService = groupService;
        this.userMapper = userMapper;
        this.userService = userService;
        this.complexUserMapper = complexUserMapper;
    }

    @PostMapping
    @PermitAll
    @Operation(summary = "Generate a new Group")
    public Long generateUserGroup(@Param("groupName") String groupName, @Param("userName") String userName) {
        LOGGER.info("POST group");
        LOGGER.info(userName + "INHERE");
        return groupService.generateUserGroup(groupName, userName);
    }

    @PutMapping
    @PermitAll
    @Operation(summary = "Add user to group")
    public void addUser(@Param("groupId") Long groupId, @Param("username") String username) {
        LOGGER.info("POST user {} to group {}", username, groupId);
        try {
            this.groupService.addUser(groupId, username);
        } catch (NotFoundException n) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, n.getMessage());
        } catch (ServiceException s) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, s.getMessage());
        }
    }


    @GetMapping("/storage")
    @PermitAll
    @Operation(summary = "Get the storageId of the currentgroup from user")
    public Long getGroupStorageForUser(Authentication authentication) {
        try {
            return userService.loadGroupStorageByUsername(authentication.getName());

        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // Todo
        }
    }

    @GetMapping("/shoppinglist")
    @PermitAll
    @Operation(summary = "Get the shoppinglistId of the currentgroup from user")
    public Long getGroupShoppinglistForUser(Authentication authentication) {
        try {
            return userService.loadGroupShoppinglistByUsername(authentication.getName());

        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()); // Todo
        }
    }


    @GetMapping
    @PermitAll
    @Operation(summary = "Get all users from group")
    public Set<UserDto> getAllUsers(@Param("groupId") Long groupId) {
        LOGGER.info("Get all users from group {}", groupId);
        try {
            return this.complexUserMapper.usersToUsersDto(this.groupService.getAllUsers(groupId));
        } catch (NotFoundException n) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, n.getMessage());
        }
    }
}
