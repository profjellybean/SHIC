package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/api/v1/group")
public class UserGroupEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final GroupService groupService;


    public UserGroupEndpoint(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    @PermitAll
    @Operation(summary = "Generate a new Group") //TODO: add security
    public Long generateUserGroup() {
        LOGGER.info("POST group");
        return groupService.generateUserGroup();
    }
}
