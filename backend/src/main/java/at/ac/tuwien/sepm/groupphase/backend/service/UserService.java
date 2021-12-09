package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
     * Find an application user based on the username.
     *
     * @param userLoginDto the DTO of the user to be created
     */
    void createUser(UserLoginDto userLoginDto);


}
