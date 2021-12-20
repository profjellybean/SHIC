package at.ac.tuwien.sepm.groupphase.backend.service;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {

    /**
     * Find a user in the context of Spring Security based on the email address
     * <br>
     * For more information have a look at this tutorial:
     * https://www.baeldung.com/spring-security-authentication-with-a-database
     *
     * @param username the username
     * @return a Spring Security user
     * @throws UsernameNotFoundException is thrown if the specified user does not exists
     */
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * Find an application user based on the username.
     *
     * @param username the username
     * @return a application user
     */

    ApplicationUser findApplicationUserByUsername(String username);

    /**
     * Gets the private shopping list by username.
     *
     * @param username username of User of required private shopping list
     * @return private shopping list
     */
    Long getPrivateShoppingListIdByUsername(String username);

    /**
     * Gets the public shopping list id by username. (Shopping list of current group)
     *
     * @param username username of User of required private shopping list
     * @return Id of public shopping list
     */
    Long getPublicShoppingListIdByUsername(String username);


    /**
     * Gets the public shopping list by username. (Shopping list of current group)
     *
     * @param username username of User of required private shopping list
     * @return public shopping list
     */
    ShoppingList getPublicShoppingListByUsername(String username);

    /**
     * Gets the private shopping list by username. (Shopping list of current group)
     *
     * @param username username of User of required private shopping list
     * @return public shopping list
     */
    ShoppingList getPrivateShoppingListByUsername(String username);

    void resendUserEmailConfirmation(String username);
    /**
     * Find an application user based on the username.
     *
     * @param userRegistrationDto the DTO of the user to be created
     *
     */

    void createUserWithEmailVerification(UserRegistrationDto userRegistrationDto);

    /**
     * Find an application user based on the username.
     * This method overloads createUser(UserRegistrationDto userRegistrationDto, Long confirmationToken)
     * confirmationToken is set to 0
     *
     * @param userRegistrationDto the DTO of the user to be created
     */
    void createUserWithoutEmailVerification(UserRegistrationDto userRegistrationDto);


    /**
     * On successful login, sets the current groupId in User.
     *
     * @param username the name of the user
     */
    void setCurrUserGroup(String username);

    /**
     * Find an application user based on the username.
     *
     * @param userRegistrationDto the DTO of the user to be created
     *
     */

    void createUser(UserRegistrationDto userRegistrationDto, Long confirmationToken);



    void confirmUser(String confirmationTokenEncrypted);

    boolean getConfirmationStatusByName(String username);

    List<ItemStorage> getCombinedAvailableItemsWithoutDuplicates(String username);
}
