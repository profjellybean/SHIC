package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.Email;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.exception.EmailConfirmationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.PasswordValidationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UsernameTakenException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EmailService;
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
import java.util.regex.PatternSyntaxException;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final CustomUserRepository userRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final UserMapper userMapper;
    private final EntityManager entityManager;
    private final EmailService emailService;
    @Autowired
    public UserServiceImpl(CustomUserRepository userRepository,ShoppingListRepository shoppingListRepository, UserMapper userMapper, EntityManager entityManager,EmailService emailService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.entityManager = entityManager;
        this.shoppingListRepository = shoppingListRepository;
        this.emailService = emailService;
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
    public void createUserWithEmailVerification(UserRegistrationDto userRegistrationDto) {

        Long confirmationToken = System.currentTimeMillis();
        createUser(userRegistrationDto,confirmationToken);
        try {

            emailService.sendEmailConfirmation(userRegistrationDto.getEmail(), userRegistrationDto.getUsername(),confirmationToken);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public void createUserWithoutEmailVerification(UserRegistrationDto userRegistrationDto) {
        createUser(userRegistrationDto,0L);
    }

    @Override
    public void createUser(UserRegistrationDto userRegistrationDto, Long confirmationToken) {
        LOGGER.debug("Service: Create new user: {}",userRegistrationDto.getUsername());


        if(!userRegistrationDto.getEmail().contains("@")){
            throw new PasswordValidationException("Wrong email format");
        }
        if(userRegistrationDto.getPassword().length() < 8){
            throw new PasswordValidationException("The password must contain at least eight characters");
        }

        Optional<ApplicationUser> applicationUser = userRepository.findUserByUsername(userRegistrationDto.getUsername());
        if (applicationUser.isPresent()) {
            throw new UsernameTakenException("Username already taken");
        }

        Long shoppingListId = shoppingListRepository.saveAndFlush(   ShoppingList.ShoppingListBuilder.aShoppingList().withName( "Your private shopping list").build()  ).getId();
        userRepository.save(userMapper.dtoToEntity(userRegistrationDto,shoppingListId, confirmationToken));




    }

    @Override
    public void confirmUser(String confirmationToken_encrypted) {
        LOGGER.debug(confirmationToken_encrypted);
        String confirmationToken_decrypted = new String(Base64.decodeBase64(confirmationToken_encrypted));
        LOGGER.debug(confirmationToken_decrypted);

        String username = confirmationToken_decrypted.split(":")[0];
        try{
            Long confirmationToken = Long.parseLong(confirmationToken_decrypted.split(":")[1]);
            if((System.currentTimeMillis() - confirmationToken) > 86400000){
                throw new EmailConfirmationException("Confirmation token expired");
            }

            Optional<ApplicationUser> user =  userRepository.findUserByUsername(username);
            if(user.isEmpty()){
                throw new EmailConfirmationException("User does not exist");
            }

            if(!user.get().getConfirmationToken().equals(confirmationToken)){
                throw new EmailConfirmationException("Wrong confirmation token");
            }

            ApplicationUser updatedUser = user.get();
            updatedUser.setConfirmationToken(0L);
            userRepository.save(updatedUser);

        }catch (NumberFormatException | PatternSyntaxException | ArrayIndexOutOfBoundsException | IllegalStateException e){
            throw new EmailConfirmationException("Wrong confirmation token");
        }




    }


}
