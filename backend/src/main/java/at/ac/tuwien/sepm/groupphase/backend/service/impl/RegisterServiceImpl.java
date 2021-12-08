package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.BillRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RegisterRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.RegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.Set;

@Service
public class RegisterServiceImpl implements RegisterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RegisterRepository registerRepository;
    private final BillRepository billRepository;
    private final UserRepository userRepository;

    public RegisterServiceImpl(RegisterRepository registerRepository, BillRepository billRepository,
                               UserRepository userRepository) {
        this.registerRepository = registerRepository;
        this.billRepository = billRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Register confirmPayment(Long registerId, Long billId, Long userId) {
        LOGGER.debug("Service: confirm Payment {}{}", registerId, userId);
        Optional<Register> registerOptional = registerRepository.findById(registerId);
        Optional<Bill> billOptional = billRepository.findById(billId);
        Optional<ApplicationUser> userOptional = userRepository.findById(userId);
        if(registerOptional.isPresent() && billOptional.isPresent() && userOptional.isPresent()) {
            Register register = registerOptional.get();
            Bill bill = billOptional.get();
            ApplicationUser user = userOptional.get();

            bill.getNotPaidNames().remove(user);
            billRepository.save(bill);
            register.getBills().add(bill);
            register = registerRepository.saveAndFlush(register);
            return register;
        } else if(registerOptional.isEmpty()){
            throw new NotFoundException(String.format("Could not find register with id %s", registerId));
        } else if(billOptional.isEmpty()) {
            throw new NotFoundException(String.format("Could not find bill with id %s", billId));
        } else {
            throw new NotFoundException(String.format("Could not find user with id %s", userId));
        }
    }

    @Transactional
    public Register findOne(Long id) {
        LOGGER.debug("Service: find register by id {}", id);
        Optional<Register> register = registerRepository.findRegisterById(id);
        if (register.isPresent()) {
            return register.get();
        } else {
            throw new NotFoundException(String.format("Could not find register with id %s", id));
        }
    }
}