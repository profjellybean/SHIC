package at.ac.tuwien.sepm.groupphase.backend.util;

import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class ItemValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserService userService;

    public ItemValidator(UserService userService) {
        this.userService = userService;
    }

    public void validate_saveCustomItem(Item item, String userName) {
        LOGGER.debug("Service: saveCustomItem item {}, userName {}", item, userName);

        if (item == null) {
            throw new ValidationException("item can not be null when saving");
        } else if (item.getName() == null) {
            throw new ValidationException("name of item can not be null when saving custom item");
        } else if (item.getQuantity() == null) {
            throw new ValidationException("unit of quantity of item can not be null when saving custom item");
        }

        if (userName == null) {
            throw new ValidationException("userName can not be null when saving custom item");
        }
        Long groupId = userService.getGroupIdByUsername(userName);
        if (groupId == null) {
            throw new ValidationException("groupId can not be null when saving custom item");
        }

    }
}
