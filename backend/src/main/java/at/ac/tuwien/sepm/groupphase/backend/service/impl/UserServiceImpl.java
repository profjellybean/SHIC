package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapperImpl;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserLoginMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.exception.EmailConfirmationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.EmailCooldownException;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.PasswordTooShortException;
import at.ac.tuwien.sepm.groupphase.backend.exception.PasswordValidationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UsernameTakenException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EmailService;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserGroupRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
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
import java.util.Set;
import java.util.regex.PatternSyntaxException;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShoppingListRepository shoppingListRepository;
    private final EntityManager entityManager;
    private final EmailService emailService;
    private final UserGroupRepository userGroupRepository;
    private final CustomUserRepository customUserRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(CustomUserRepository userRepository,
                           ShoppingListRepository shoppingListRepository,
                           UserMapperImpl userMapper, EntityManager entityManager, UserLoginMapper userLoginMapper,
                           EmailService emailService, UserGroupRepository userGroupRepository) {
        this.customUserRepository = userRepository;
        this.userMapper = userMapper;
        this.entityManager = entityManager;
        this.shoppingListRepository = shoppingListRepository;
        this.emailService = emailService;
        this.userGroupRepository = userGroupRepository;

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
        Optional<ApplicationUser> applicationUser = customUserRepository.findUserByUsername(username);
        if (applicationUser.isPresent()) {
            return applicationUser.get();
        }
        throw new NotFoundException(String.format("Could not find the user with the username %s", username));
    }

    @Override
    public Long getPrivateShoppingListIdByUsername(String username) {
        LOGGER.debug("Service: Find private shoppinglist for user by username");
        Optional<ApplicationUser> applicationUser = customUserRepository.findUserByUsername(username);
        if (applicationUser.isPresent()) {
            return applicationUser.get().getPrivList();
        }
        throw new NotFoundException(String.format("Could not find the user with the username %s", username));
    }

    @Override
    public void createUser(UserRegistrationDto userRegistrationDto, Long confirmationToken) {
        LOGGER.debug("Service: Create new user: {}", userRegistrationDto.getUsername());


        if (!userRegistrationDto.getEmail().contains("@")) {
            throw new PasswordValidationException("Wrong email format");
        }
        if (userRegistrationDto.getPassword().length() < 8) {
            throw new PasswordValidationException("The password must contain at least eight characters");
        }

        Optional<ApplicationUser> applicationUser = customUserRepository.findUserByUsername(userRegistrationDto.getUsername());
        if (applicationUser.isPresent()) {
            throw new UsernameTakenException("Username already taken");
        }

        Long shoppingListId = shoppingListRepository.saveAndFlush(ShoppingList.ShoppingListBuilder.aShoppingList().withName("Your private shopping list").build()).getId();
        customUserRepository.save(userMapper.dtoToEntity(userRegistrationDto, shoppingListId, confirmationToken));


    }


    @Override
    public void resendUserEmailConfirmation(String username) {
        Optional<ApplicationUser> applicationUser = customUserRepository.findUserByUsername(username);
        if (applicationUser.isEmpty()) {
            throw new NotFoundException("User not in database");
        }
        ApplicationUser user = applicationUser.get();
        Long confirmationToken = System.currentTimeMillis();


        if ((confirmationToken - user.getConfirmationToken()) <= 300000L) {
            throw new EmailCooldownException("Email confirmations can be sent only once in 5 minutes");
        }
        try {
            emailService.sendEmailConfirmation(user.getEmail(), user.getUsername(), confirmationToken);
            user.setConfirmationToken(confirmationToken);
            LOGGER.info("::: " + user.getConfirmationToken());
            customUserRepository.saveAndFlush(user);

            applicationUser = customUserRepository.findUserByUsername(username);
            LOGGER.info("::: " + applicationUser.get().getConfirmationToken());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }



    }

    @Override
    public void createUserWithEmailVerification(UserRegistrationDto userRegistrationDto) {

        Long confirmationToken = System.currentTimeMillis();
        createUser(userRegistrationDto, confirmationToken);
        try {
            emailService.sendEmailConfirmation(userRegistrationDto.getEmail(), userRegistrationDto.getUsername(), confirmationToken);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void createUserWithoutEmailVerification(UserRegistrationDto userRegistrationDto) {
        createUser(userRegistrationDto, 0L);
    }


    @Override
    public void setCurrUserGroup(String username) {
        LOGGER.debug("Service: set current group in user: {}", username);
        List<UserGroup> userGroups = userGroupRepository.findAll();
        for (UserGroup u : userGroups) {
            Set<ApplicationUser> users = u.getUser();
            for (ApplicationUser applicationUser : users) {
                if (applicationUser.getUsername().equals(username)) {
                    Optional<ApplicationUser> temp = customUserRepository.findUserByUsername(username);
                    if (temp.isPresent()) {
                        temp.get().setCurrGroup(u);
                        customUserRepository.saveAndFlush(temp.get());
                    }
                }
            }
        }
    }



    @Override
    public void confirmUser(String confirmationTokenEncrypted) {
        LOGGER.debug(confirmationTokenEncrypted);
        String confirmationTokenDecrypted = new String(Base64.decodeBase64(confirmationTokenEncrypted));
        LOGGER.debug(confirmationTokenDecrypted);

        String username = confirmationTokenDecrypted.split(":")[0];
        try {
            Long confirmationToken = Long.parseLong(confirmationTokenDecrypted.split(":")[1]);
            if ((System.currentTimeMillis() - confirmationToken) > 86400000) {
                throw new EmailConfirmationException("Confirmation token expired");
            }

            Optional<ApplicationUser> user =  customUserRepository.findUserByUsername(username);
            if (user.isEmpty()) {
                throw new EmailConfirmationException("User does not exist");
            }

            if (!user.get().getConfirmationToken().equals(confirmationToken)) {
                throw new EmailConfirmationException("Wrong confirmation token");
            }

            ApplicationUser updatedUser = user.get();
            updatedUser.setConfirmationToken(0L);
            customUserRepository.save(updatedUser);

        } catch (NumberFormatException | PatternSyntaxException | ArrayIndexOutOfBoundsException | IllegalStateException e) {
            throw new EmailConfirmationException("Wrong confirmation token");
        }




    }

    @Override
    public boolean getConfirmationStatusByName(String username) {
        LOGGER.info("2:::");
        Optional<ApplicationUser> user =  customUserRepository.findUserByUsername(username);
        if (user.isEmpty()) {
            return false;
        }
        LOGGER.info("Ergebjis: " + (user.get().getConfirmationToken().equals(0L)) + "   " + user.get().getConfirmationToken());
        return user.get().getConfirmationToken() == 0L;

    }



}
