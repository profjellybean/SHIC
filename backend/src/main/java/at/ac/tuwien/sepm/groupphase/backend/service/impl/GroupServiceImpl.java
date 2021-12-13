package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserGroupRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.Set;

@Service
public class GroupServiceImpl implements GroupService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserGroupRepository userGroupRepository;
    private final StorageRepository storageRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final UserRepository userRepository;

    public GroupServiceImpl(UserGroupRepository userGroupRepository, StorageRepository storageRepository, ShoppingListRepository shoppingListRepository, UserRepository userRepository) {
        this.userGroupRepository = userGroupRepository;
        this.storageRepository = storageRepository;
        this.shoppingListRepository = shoppingListRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Long generateUserGroup() {
        LOGGER.debug("Generate new group");
        UserGroup group = new UserGroup(this.storageRepository.saveAndFlush(new Storage()).getId(), this.shoppingListRepository.save(new ShoppingList()).getId());
        return this.userGroupRepository.saveAndFlush(group).getId();
    }

    @Override
    public void addUser(Long groupId, ApplicationUser applicationUser) {
        LOGGER.debug("Add user {} to group {}", applicationUser.getUsername(), groupId);
        UserGroup userGroup = this.userGroupRepository.getById(groupId);
        Set<ApplicationUser> user = userGroup.getUser();
        Optional<ApplicationUser> temp = this.userRepository.findUserByUsername(applicationUser.getUsername());
        if (temp.isPresent()) {
            user.add(applicationUser);
            userGroup.setUser(user);
            this.userGroupRepository.saveAndFlush(userGroup);
        } else {
            throw new NotFoundException("User " + applicationUser.getUsername() + " was not found!");
        }

    }
}
