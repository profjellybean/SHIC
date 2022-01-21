package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EmailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ComplexUserMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserLoginMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.PendingEmail;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UsernameTakenException;
import at.ac.tuwien.sepm.groupphase.backend.exception.PasswordValidationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.EmailConfirmationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.EmailCooldownException;

import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;
import at.ac.tuwien.sepm.groupphase.backend.repository.BillRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PendingEmailRespository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EmailService;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserGroupRepository;
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
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ComplexUserMapper userMapperImpl;
    private final BillRepository billRepository;
    private final PendingEmailRespository pendingEmailRespository;

    @Autowired
    public UserServiceImpl(CustomUserRepository userRepository,
                           ShoppingListRepository shoppingListRepository,
                           UserMapper userMapper, EntityManager entityManager, UserLoginMapper userLoginMapper,
                           EmailService emailService, UserGroupRepository userGroupRepository, UserRepository userRepository1,
                           ComplexUserMapper userMapperImpl, BillRepository billRepository, PendingEmailRespository pendingEmailRespository) {
        this.customUserRepository = userRepository;
        this.userMapper = userMapper;
        this.userRepository = userRepository1;
        this.userMapperImpl = userMapperImpl;
        this.entityManager = entityManager;
        this.shoppingListRepository = shoppingListRepository;
        this.emailService = emailService;
        this.userGroupRepository = userGroupRepository;
        this.pendingEmailRespository = pendingEmailRespository;
        this.billRepository = billRepository;
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
    public Long getPublicShoppingListIdByUsername(String username) {
        LOGGER.debug("Service: Find private shoppinglist for user by username");
        Optional<ApplicationUser> applicationUser = customUserRepository.findUserByUsername(username);
        if (applicationUser.isPresent()) {
            return customUserRepository.getPublicShoppingListIdByName(username);
        }
        throw new NotFoundException(String.format("Could not find the user with the username %s", username));
    }

    @Override
    public ShoppingList getPublicShoppingListByUsername(String username) {
        LOGGER.debug("Service: Find public shoppinglist for user by username");
        Optional<ApplicationUser> applicationUser = customUserRepository.findUserByUsername(username);
        if (applicationUser.isPresent()) {
            return customUserRepository.getPublicShoppingListByName(username);
        }
        throw new NotFoundException(String.format("Could not find the user with the username %s", username));
    }

    @Override
    public ShoppingList getPrivateShoppingListByUsername(String username) {
        LOGGER.debug("Service: Find private shoppinglist for user by username");
        Optional<ApplicationUser> applicationUser = customUserRepository.findUserByUsername(username);
        if (applicationUser.isPresent()) {
            return customUserRepository.getPrivateShoppingListByName(username);
        }
        throw new NotFoundException(String.format("Could not find the user with the username %s", username));
    }

    @Override
    public List<ItemStorage> getCombinedAvailableItemsWithoutDuplicates(String username) {
        LOGGER.debug("Service: Gather available items from both shopping lists");
        Optional<ApplicationUser> applicationUser = customUserRepository.findUserByUsername(username);
        if (applicationUser.isPresent()) {
            return customUserRepository.getAvailableItems(username);
        }
        throw new NotFoundException(String.format("Could not find the user with the username %s", username));
    }

    @Override
    public Long getGroupIdByUsername(String username) {
        LOGGER.debug("Service: Get id of Users current Group by Username");
        return customUserRepository.loadGroupIdByUsername(username);
    }

    @Override
    public void deleteUserById(Long id) {
        ApplicationUser applicationUser = userRepository.getById(id);
        List<Bill> bills = billRepository.findAll();
        for (Bill b : bills) {
            if (b.getNames().contains(applicationUser)) {
                Set<ApplicationUser> names = b.getNames();
                names.remove(applicationUser);
                b.setNames(names);
                billRepository.saveAndFlush(b);
            }
        }

        if (applicationUser != null) {
            UserGroup userGroup = applicationUser.getCurrGroup();
            if (userGroup != null) {
                Set<ApplicationUser> users = userGroup.getUser();
                users.remove(applicationUser);
                userGroup.setUser(users);
                userGroupRepository.saveAndFlush(userGroup);

            } else {
                throw new ServiceException("User with id " + id + "is in no group!");
            }
        } else {
            throw new NotFoundException("User " + id + " was not found!");
        }

        this.userRepository.deleteById(id);
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
        customUserRepository.save(userMapperImpl.registrationDtoToApplicationUser(userRegistrationDto, shoppingListId, confirmationToken));


    }

    @Override
    public Long changeEmail(EmailDto emailDto, String username) {
        Long confirmationToken = System.currentTimeMillis();

        try {
            PendingEmail pendingEmail = new PendingEmail(emailDto.getEmail(), confirmationToken, username);
            pendingEmailRespository.save(pendingEmail);
            emailService.sendEmailChangeConfirmation(emailDto.getEmail(), username, confirmationToken);
            return confirmationToken;

        } catch (Exception e) {
            throw new EmailConfirmationException("Email could not be sent");
        }
    }

    @Override
    public void editPicture(byte[] picture, String username) {
        Optional<ApplicationUser> u = customUserRepository.findUserByUsername(username);
        if (u.isEmpty()) {
            throw new NotFoundException("Username not found");
        }
        ApplicationUser user = u.get();
        user.setImage(picture);
        customUserRepository.saveAndFlush(user);
    }


    @Override
    public void editUsername(String newUsername, String username) {
        if (newUsername.isBlank()) {
            throw new UsernameTakenException("Username cannot be emtpy");
        }
        Optional<ApplicationUser> u = customUserRepository.findUserByUsername(newUsername);
        if (u.isPresent()) {
            throw new UsernameTakenException("This username is already taken");
        }

        Optional<ApplicationUser> userO = customUserRepository.findUserByUsername(username);
        if (userO.isEmpty()) {
            throw new NotFoundException("Username not found");
        }

        ApplicationUser user = userO.get();
        user.setUsername(newUsername);
        customUserRepository.saveAndFlush(user);
    }

    @Override
    public Long loadGroupStorageByUsername(String username) {
        return customUserRepository.loadGroupStorageByUsername(username);
    }

    @Override
    public Long loadGroupShoppinglistByUsername(String username) {
        return customUserRepository.loadGroupShoppinglistByUsername(username);
    }

    @Override
    public Long loadGroupRegisterIdByUsername(String username) {
        return customUserRepository.loadGroupRegisterIdByUsername(username);
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

    public void confirmNewEmail(String confirmationTokenEncrypted) {
        String confirmationTokenDecrypted = new String(Base64.decodeBase64(confirmationTokenEncrypted));

        String username = confirmationTokenDecrypted.split(":")[0];

        try {
            Long confirmationToken = Long.parseLong(confirmationTokenDecrypted.split(":")[1]);
            if ((System.currentTimeMillis() - confirmationToken) > 86400000) {
                throw new EmailConfirmationException("Confirmation token expired");
            }

            Optional<ApplicationUser> userO = customUserRepository.findUserByUsername(username);
            Optional<PendingEmail> pendingEmail = pendingEmailRespository.findEmailByConfirmationToken(confirmationToken, username);

            if (userO.isEmpty()) {
                throw new EmailConfirmationException("User does not exist");
            }

            if (pendingEmail.isEmpty()) {
                throw new EmailConfirmationException("No new Email detected in database");
            }

            String email = pendingEmail.get().getEmail();

            ApplicationUser user = userO.get();
            user.setEmail(email);
            customUserRepository.save(user);
            pendingEmailRespository.deleteByEmail(email);

        } catch (NumberFormatException | PatternSyntaxException | ArrayIndexOutOfBoundsException | IllegalStateException e) {
            throw new EmailConfirmationException("Wrong confirmation token");
        }

    }

    @Override
    public void confirmUser(String confirmationTokenEncrypted) {

        String confirmationTokenDecrypted = new String(Base64.decodeBase64(confirmationTokenEncrypted));

        String username = confirmationTokenDecrypted.split(":")[0];
        try {
            Long confirmationToken = Long.parseLong(confirmationTokenDecrypted.split(":")[1]);
            if ((System.currentTimeMillis() - confirmationToken) > 86400000) {
                throw new EmailConfirmationException("Confirmation token expired");
            }

            Optional<ApplicationUser> user = customUserRepository.findUserByUsername(username);
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
        Optional<ApplicationUser> user = customUserRepository.findUserByUsername(username);
        if (user.isEmpty()) {
            return false;
        }
        LOGGER.info("Ergebjis: " + (user.get().getConfirmationToken().equals(0L)) + "   " + user.get().getConfirmationToken());
        return user.get().getConfirmationToken() == 0L;

    }


}
