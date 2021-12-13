package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/api/v1/group")
public class UserGroupEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final GroupService groupService;
    private final UserMapper userMapper;


    public UserGroupEndpoint(GroupService groupService, UserMapper userMapper) {
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
    public void addUser(@Param("groupId") Long groupId, @Valid @RequestBody UserDto userDto) {
        LOGGER.info("POST user {} to group {}", userDto.getUsername(), groupId);
        this.groupService.addUser(groupId, userMapper.userDtoToUser(userDto));
    }
}
