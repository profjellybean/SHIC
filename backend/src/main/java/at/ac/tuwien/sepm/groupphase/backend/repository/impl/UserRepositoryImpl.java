package at.ac.tuwien.sepm.groupphase.backend.repository.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.UsernameTakenException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//TODO: replace this class with a correct ApplicationUser JPARepository implementation
@Transactional
@Repository

public class UserRepositoryImpl implements UserRepository {

    private ApplicationUser user;
    private ApplicationUser admin;

    @PersistenceContext
    private final EntityManager entityManager;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    public UserRepositoryImpl( EntityManager entityManager, PasswordEncoder passwordEncoder) {

        this.entityManager = entityManager;
        user = new ApplicationUser("user@email.com", passwordEncoder.encode("password"));
        admin = new ApplicationUser("admin@email.com", passwordEncoder.encode("password"));
       // user = new ApplicationUser("username","password");
    }

    @Override
    public Optional<ApplicationUser> findUserByUsername(String username) {
        if (username.equals(user.getUsername())) {
            return Optional.of(user);
        }
        if (username.equals(admin.getUsername())) {
            return Optional.of(admin);
        }
        return Optional.empty();

    }


    @Override
    public void createUser(ApplicationUser newUser) {

        LOGGER.debug("Repository: UserRepositoryImpl createUser {}", newUser.getUsername());
        TypedQuery<ApplicationUser> query = entityManager.createNamedQuery("findByName", ApplicationUser.class);
        query.setParameter("username",newUser.getUsername());

        if(query.getResultList().size() > 0) throw new UsernameTakenException("Username already taken");

        entityManager.persist(newUser);

    }

}
