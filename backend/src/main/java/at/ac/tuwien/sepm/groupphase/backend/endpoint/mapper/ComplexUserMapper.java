package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserGroupDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoggedInDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class ComplexUserMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ComplexUserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public ApplicationUser registrationDtoToApplicationUser(UserRegistrationDto user, Long shoppingListId, Long confirmationToken) {
        LOGGER.debug("Mapper: User dtoToEntity");
        return new ApplicationUser(user.getUsername(), passwordEncoder.encode(user.getPassword()), shoppingListId, user.getEmail(), confirmationToken);
    }

    public ApplicationUser registrationDtoToApplicationUser(UserRegistrationDto user, Long shoppingListId) {
        LOGGER.debug("Mapper: User dtoToEntity");
        return new ApplicationUser(user.getUsername(), passwordEncoder.encode(user.getPassword()), shoppingListId, user.getEmail());
    }

    public UserLoginDto entityToDto(ApplicationUser user) {
        LOGGER.debug("Mapper: User entityToDto");
        return new UserLoginDto(user.getUsername(), user.getPassword());
    }

    public UserLoggedInDto entityToLoggedInDto(ApplicationUser user) {
        LOGGER.debug("Mapper: User entityToLoggedInDto");
        return new UserLoggedInDto(user.getId(), user.getUsername(), user.getPrivList());
    }

    public UserDto userToUserDto(ApplicationUser user) {
        LOGGER.debug("Mapper: User userToUserDto");
        UserGroupDto group = null;
        if (user.getCurrGroup() != null) {
            LinkedHashSet<String> usernames = new LinkedHashSet<>();
            Set<ApplicationUser> users = user.getCurrGroup().getUser();
            for (ApplicationUser a : users) {
                usernames.add(a.getUsername());
            }
            group = new UserGroupDto(user.getCurrGroup().getId(), usernames,
                user.getCurrGroup().getStorageId(), user.getCurrGroup().getPublicShoppingListId(), user.getCurrGroup().getRegisterId(), user.getCurrGroup().getName());
        }
        return new UserDto(user.getId(), user.getUsername(), group, user.getPrivList(), user.getEmail(), user.getImage());
    }

    public Set<UserDto> usersToUsersDto(Set<ApplicationUser> allUsers) {
        if (allUsers == null) {
            return null;
        }

        LinkedHashSet<UserDto> list = new LinkedHashSet<>();
        for (ApplicationUser a : allUsers) {
            list.add(userToUserDto(a));
        }

        return list;
    }
}

