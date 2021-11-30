package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
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

    public ApplicationUser dtoToEntity(UserLoginDto user){
        LOGGER.debug("Mapper: User dtoToEntity" );
        return  new ApplicationUser(user.getUsername(),passwordEncoder.encode(user.getPassword()));
    }

    public UserLoginDto entityToDto(ApplicationUser user){
        LOGGER.debug("Mapper: User entityToDto" );
        return  new UserLoginDto(user.getUsername(),user.getPassword());
    }

}