package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.BillRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RegisterRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.BillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BillServiceImpl implements BillService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final BillRepository billRepository;
    private final RegisterRepository registerRepository;
    private final UserRepository userRepository;
    private final ItemStorageRepository storageRepository;

    public BillServiceImpl(BillRepository billRepository, RegisterRepository registerRepository, UserRepository userRepository, ItemStorageRepository storageRepository) {
        this.billRepository = billRepository;
        this.registerRepository = registerRepository;
        this.userRepository = userRepository;
        this.storageRepository = storageRepository;
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
    public List<Bill> findAll(Long registerId) {
        return billRepository.findAllByRegisterId(registerId);
    }

    @Override
    public void deleteById(Long id) {
        Bill bill = billRepository.getById(id);
        Register register = registerRepository.getById(bill.getRegisterId());
        if (bill != null) {
            if (register.getBills().contains(bill)) {
                Set<Bill> billsInRegister = register.getBills();
                billsInRegister.remove(bill);
                register.setBills(billsInRegister);
                registerRepository.saveAndFlush(register);
            }
            if (!bill.getNames().isEmpty()) {
                bill.setNames(null);
            }
            if (!bill.getNotPaidNames().isEmpty()) {
                bill.setNotPaidNames(null);
            }

            billRepository.saveAndFlush(bill);
            billRepository.deleteById(id);
        }
    }

    @Override
    public Bill bill(Bill bill) {
        LOGGER.debug("Service: create new bill");
        if (bill.getGroceries() != null) {
            Set<ItemStorage> items = bill.getGroceries();
            HashSet<ItemStorage> newItems = new HashSet<>();
            for (ItemStorage i : items) {
                ItemStorage temp = new ItemStorage(i.getName());
                this.storageRepository.saveAndFlush(temp);
                newItems.add(temp);
            }
            bill.setGroceries(newItems);
        }
        double sumPerPerson = bill.getSumPerPerson();
        sumPerPerson = (double) Math.round(sumPerPerson * 100) / 100.00;
        bill.setSumPerPerson(sumPerPerson);

        double sum = bill.getSum();
        sum = (double) Math.round(sum * 100) / 100.00;
        bill.setSum(sum);


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

    @Override
    public Bill updateBill(Bill bill) {
        LOGGER.debug("Service: update a bill");
        if (bill.getId() == null) {
            throw new ValidationException("Bill's id is null");
        }

        return this.billRepository.saveAndFlush(bill);
    }
}
