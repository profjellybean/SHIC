package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.PasswordTooShortException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UsernameTakenException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.impl.UserRepositoryImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        LOGGER.debug("Service: Load all user by email");
        try {
            ApplicationUser applicationUser = findApplicationUserByUsername(username);

            List<GrantedAuthority> grantedAuthorities;
            grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");


            return new User(applicationUser.getUsername(), applicationUser.getPassword(), grantedAuthorities);
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public ApplicationUser findApplicationUserByUsername(String username) {
        LOGGER.debug("Service: Find application user by username");
        Optional<ApplicationUser> applicationUser = userRepository.findUserByUsername(username);
        if (applicationUser.isPresent()) {
            return applicationUser.get();
        }
        throw new NotFoundException(String.format("Could not find the user with the username %s", username));
    }

    @Override
    public void createUser(UserLoginDto userLoginDto) {
        if(userLoginDto.getPassword().length() < 8){
            throw new PasswordTooShortException("The password must contain at least eight characters");
        }

        LOGGER.debug("Create new user: {}",userLoginDto.getUsername());
        userRepository.createUser(userMapper.dtoToEntity(userLoginDto));



    }



}
