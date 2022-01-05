package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.BillRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RegisterRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.BillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BillServiceImpl implements BillService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final BillRepository billRepository;
    private final RegisterRepository registerRepository;
    private final UserRepository userRepository;

    public BillServiceImpl(BillRepository billRepository, RegisterRepository registerRepository, UserRepository userRepository) {
        this.billRepository = billRepository;
        this.registerRepository = registerRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public Bill findOne(Long id) {
        LOGGER.debug("Service: find bill by id {}", id);
        Optional<Bill> bill = billRepository.findById(id);
        if (bill.isPresent()) {
            return bill.get();
        } else {
            throw new NotFoundException(String.format("Could not find bill with id %s", id));

        }
    }

    @Transactional
    @Override
    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    @Override
    public Bill bill(Bill bill) {
        LOGGER.debug("Service: create new bill");
        Bill savedBill = billRepository.saveAndFlush(bill);
        Optional<Register> register = registerRepository.findRegisterById(bill.getRegisterId());
        if (register.isPresent()) {
            Set<Bill> bills = register.get().getBills();
            bills.add(savedBill);
            register.get().setBills(bills);
            registerRepository.saveAndFlush(register.get());
        }
        return savedBill;
    }
}
