package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapperImpl;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/api/v1/group")
public class UserGroupEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final GroupService groupService;
    private final UserMapperImpl userMapper;


    public UserGroupEndpoint(GroupService groupService, UserMapperImpl userMapper) {
        this.groupService = groupService;
        this.userMapper = userMapper;
    }

    @PostMapping
    @PermitAll
    @Operation(summary = "Generate a new Group")
    public Long generateUserGroup() {
        LOGGER.info("POST group");
        return groupService.generateUserGroup();
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
            throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, s.getMessage());
        }
    }
}
