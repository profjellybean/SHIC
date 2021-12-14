package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;

public interface GroupService {
    /**
     * Generates a new UserGroup containing of a group ID, a storageId(the storage is also generated here), a public shopping list id (is generated here) and a set of valid users.
     *
     * @return the UserGroupId
     */
    Long generateUserGroup();

    /**
     * Adds the application user to the list of users in the group.
     *
     * @param groupId  the group where to add to
     * @param username the application username
     */
    void addUser(Long groupId, String username);
}
