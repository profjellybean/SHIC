package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserLoginMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.PasswordTooShortException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UsernameTakenException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
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

import javax.persistence.EntityManager;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final CustomUserRepository userRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final UserLoginMapper userLoginMapper;
    private final EntityManager entityManager;

    @Autowired
    public UserServiceImpl(CustomUserRepository userRepository, ShoppingListRepository shoppingListRepository, UserLoginMapper userLoginMapper, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.userLoginMapper = userLoginMapper;
        this.entityManager = entityManager;
        this.shoppingListRepository = shoppingListRepository;
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
    public Long getPrivateShoppingListIdByUsername(String username) {
        LOGGER.debug("Service: Find private shoppinglist for user by username");
        Optional<ApplicationUser> applicationUser = userRepository.findUserByUsername(username);
        if (applicationUser.isPresent()) {
            return applicationUser.get().getPrivList();
        }
        throw new NotFoundException(String.format("Could not find the user with the username %s", username));
    }


    @Override
    public void createUser(UserLoginDto userLoginDto) {
        LOGGER.debug("Service: Create new user: {}", userLoginDto.getUsername());

        if (userLoginDto.getPassword().length() < 8) {
            throw new PasswordTooShortException("The password must contain at least eight characters");
        }

        Optional<ApplicationUser> applicationUser = userRepository.findUserByUsername(userLoginDto.getUsername());
        if (applicationUser.isPresent()) {
            throw new UsernameTakenException("Username already taken");
        }

        Long shoppingListId = shoppingListRepository.saveAndFlush(ShoppingList.ShoppingListBuilder.aShoppingList().withName("Your private shopping list").build()).getId();
        userRepository.save(userLoginMapper.dtoToEntity(userLoginDto, shoppingListId));


    }


}
