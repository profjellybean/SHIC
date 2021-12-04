package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.BillRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.BillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

@Service
public class BillServiceImpl implements BillService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final BillRepository billRepository;
    private final UserRepository userRepository;

    public BillServiceImpl(BillRepository billRepository, UserRepository userRepository) {
        this.billRepository = billRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Bill findOne(Long id) {
        LOGGER.debug("Service: find bill by id {}", id);
        Optional<Bill> bill = billRepository.findById(id);
        if(bill.isPresent()) {
            return bill.get();
        } else {
            throw new NotFoundException(String.format("Could not find bill with id %s", id));

        }
    }

    @Override
    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    @Override
    public Bill deleteNames(Long billId, Long userId) {
        Optional <ApplicationUser> user = userRepository.findById(userId);
        if(user.isPresent()) {
            Bill bill = findOne(billId);
            bill.getNotPaidNames().remove(user.get());
            billRepository.saveAndFlush(bill);
            return null;
        } else {
            throw new NotFoundException(String.format("Could not find user with id %s", userId));
        }
    }
}
