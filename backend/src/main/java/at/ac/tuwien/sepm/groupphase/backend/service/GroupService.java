package at.ac.tuwien.sepm.groupphase.backend.service;

public interface GroupService {
    /**
     * Generates a new UserGroup containing of a group ID, a storageId(the storage is also generated here), a public shopping list id (is generated here) and a set of valid users.
     *
     * @return the UserGroupId
     */
    Long generateUserGroup();
}
