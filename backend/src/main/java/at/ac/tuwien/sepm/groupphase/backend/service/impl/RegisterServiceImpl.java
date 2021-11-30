package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.Message;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.RegisterRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.RegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class RegisterServiceImpl implements RegisterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RegisterRepository registerRepository;

    public RegisterServiceImpl(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    @Override
    public Register confirmPayment(Long registerId, Long billId, Long userId) {
        LOGGER.debug("Service: confirm Payment {}{}", registerId, userId);
        Optional<Register> registerOptional = registerRepository.findById(registerId);
        if(registerOptional.isPresent()) {
            Register register = registerOptional.get();
            Map<Long, Bill> bills = register.getBills();
            Bill bill = bills.get(billId);
            bills.remove(bill);
            Map<Long, ApplicationUser> all = bill.getNotPaidNames();
            ApplicationUser user = all.get(userId);
            all.remove(user);
            bill.setNotPaidNames(all);
            bills.put(billId, bill);
            register.setBills(bills);
            register = registerRepository.save(register);
            return register;
        } else {
            throw new NotFoundException(String.format("Could not find register with id %s", registerId));
        }
    }

    public Register findOne(Long id) {
        LOGGER.debug("Service: find register by id {}", id);
        Optional<Register> register = registerRepository.findById(id);
        if (register.isPresent()) {
            return register.get();
        } else {
            throw new NotFoundException(String.format("Could not find register with id %s", id));
        }
    }
}