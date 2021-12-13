package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

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

@Component
public class UserMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public ApplicationUser dtoToEntity(UserRegistrationDto user, Long shoppingListId, Long confirmationToken){
        LOGGER.debug("Mapper: User dtoToEntity" );
        return  new ApplicationUser(user.getUsername(),passwordEncoder.encode(user.getPassword()), shoppingListId,user.getEmail(), confirmationToken);
    }

    public ApplicationUser dtoToEntity(UserRegistrationDto user, Long shoppingListId){
        LOGGER.debug("Mapper: User dtoToEntity" );
        return  new ApplicationUser(user.getUsername(),passwordEncoder.encode(user.getPassword()), shoppingListId,user.getEmail());
    }

    public UserLoginDto entityToDto(ApplicationUser user){
        LOGGER.debug("Mapper: User entityToDto" );
        return  new UserLoginDto(user.getUsername(),user.getPassword());
    }

    public UserLoggedInDto entityToLoggedInDto(ApplicationUser user){
        LOGGER.debug("Mapper: User entityToLoggedInDto" );
        return new UserLoggedInDto(user.getId(),user.getUsername(), user.getPrivList());
    }

}
