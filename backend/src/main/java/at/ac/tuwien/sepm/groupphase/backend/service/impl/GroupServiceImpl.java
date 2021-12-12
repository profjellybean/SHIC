package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserGroupRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class GroupServiceImpl implements GroupService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserGroupRepository userGroupRepository;
    private final StorageRepository storageRepository;
    private final ShoppingListRepository shoppingListRepository;

    public GroupServiceImpl(UserGroupRepository userGroupRepository, StorageRepository storageRepository, ShoppingListRepository shoppingListRepository) {
        this.userGroupRepository = userGroupRepository;
        this.storageRepository = storageRepository;
        this.shoppingListRepository = shoppingListRepository;
    }


    @Override
    public Long generateUserGroup() {
        LOGGER.debug("Generate new group");
        UserGroup group = new UserGroup(this.storageRepository.saveAndFlush(new Storage()).getId(), this.shoppingListRepository.save(new ShoppingList()).getId());
        return this.userGroupRepository.saveAndFlush(group).getId();
    }
}
