package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.BillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

@Service
public class BillServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final BillRepository billRepository;

    public BillServiceImpl(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public Bill finOne(Long id) {
        LOGGER.debug("Service: find bill by id {}", id);
        Optional<Bill> bill = billRepository.findById(id);
        if(bill.isPresent()) {
            return bill.get();
        } else {
            throw new NotFoundException(String.format("Could not find bill with id %s", id));

        }
    }

    private List<Bill> findAll() {
        return billRepository.findAll();
    }
}
