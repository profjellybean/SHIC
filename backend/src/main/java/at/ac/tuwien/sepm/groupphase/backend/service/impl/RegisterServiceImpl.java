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
    public Register confirmPayment(Register register, Bill bill, ApplicationUser user) {
        LOGGER.debug("Service: confirm Payment {}{}", register, user);
        Set<Bill> bills = register.getBills();
        bills.remove(bill);
        Set<ApplicationUser> all = bill.getNotPaidNames();
        all.remove(user);
        bill.setNotPaidNames(all);
        bills.add(bill);
        return register;
    }

    public Register findOne(Long id) {
        LOGGER.debug("Service: find register by id {}", id);
        Optional<Register> register = registerRepository.findById(id);
        if (register.isPresent()) {
            return register.get();
        } else {
            throw new NotFoundException(String.format("Could not find message with id %s", id));
        }

    }
}