package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
import at.ac.tuwien.sepm.groupphase.backend.entity.UserGroup;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.BillRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RegisterRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserGroupRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.RegisterService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class RegisterServiceImpl implements RegisterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RegisterRepository registerRepository;
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public RegisterServiceImpl(RegisterRepository registerRepository, BillRepository billRepository,
                               UserRepository userRepository, UserService userService) {
        this.registerRepository = registerRepository;
        this.billRepository = billRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @Override
    public Register confirmPayment(Long registerId, Long billId, String username) {
        LOGGER.debug("Service: confirm Payment {}{}", registerId, username);
        Optional<Register> registerOptional = registerRepository.findById(registerId);
        Optional<Bill> billOptional = billRepository.findById(billId);
        Optional<ApplicationUser> userOptional = userRepository.findUserByUsername(username);
        if (registerOptional.isPresent() && billOptional.isPresent() && userOptional.isPresent()) {
            Register register = registerOptional.get();
            Bill bill = billOptional.get();
            ApplicationUser user = userOptional.get();

            bill.getNotPaidNames().remove(user);
            billRepository.save(bill);
            register.getBills().add(bill);
            register = registerRepository.saveAndFlush(register);
            return register;
        } else if (registerOptional.isEmpty()) {
            throw new NotFoundException(String.format("Could not find register with id %s", registerId));
        } else if (billOptional.isEmpty()) {
            throw new NotFoundException(String.format("Could not find bill with id %s", billId));
        } else {
            throw new NotFoundException(String.format("Could not find user with username %s", username));
        }
    }

    @Transactional
    @Override
    public Register findOne(Long id) {
        LOGGER.debug("Service: find register by id {}", id);
        Optional<Register> register = registerRepository.findRegisterById(id);
        if (register.isPresent()) {
            return register.get();
        } else {
            throw new NotFoundException(String.format("Could not find register with id %s", id));
        }
    }

    @Transactional
    @Override
    public Double billSumOfCurrentMonth(String userName) {
        LOGGER.debug("Service: get sum of Bills of current month");

        Long registerId = userService.loadGroupRegisterIdByUsername(userName);
        if (registerId == null) {
            throw new NotFoundException("No register found for User " + userName);
        }
        Double sum = billRepository.billSumOfCurrentMonth(registerId, LocalDate.now());
        if (sum == null) {
            return 0.0;
        }
        return sum;
    }

    @Transactional
    @Override
    public Double editMonthlyBudget(String userName, Double newBudget) {
        LOGGER.debug("Service: edit monthlyBudget {} of register of Group with user {}", userName, newBudget);

        if (userName == null) {
            throw new ValidationException("No User specified");
        }
        if (newBudget == null) {
            throw new ValidationException("No Budget specified");
        }
        if (newBudget < 0) {
            throw new ValidationException("Monthly Budget has to be greater than 0");
        }
        Long registerId = userService.loadGroupRegisterIdByUsername(userName);
        if (registerId == null) {
            throw new NotFoundException("No register found for User " + userName);
        }
        System.out.println("AAAAAAAAA REGISTERID " + registerId);
        System.out.println("AAAAAAAAA ALL " + registerRepository.findAll());
        Optional<Register> registerOptional = registerRepository.findRegisterById(registerId);
        if (registerOptional.isPresent()) {
            Register register = registerOptional.get();
            register.setMonthlyBudget(newBudget);
            registerRepository.save(register);
            return newBudget;
        }
        throw new NotFoundException("No register found for User " + userName);
    }


}