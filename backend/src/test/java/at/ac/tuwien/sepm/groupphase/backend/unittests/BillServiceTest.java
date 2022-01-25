package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.BillRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.BillService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class BillServiceTest {
    @Autowired
    private BillService billService;
    @Autowired
    private BillRepository billRepository;


    @Test
    public void findBillThatDoesntExistShouldThrowNotFound() {
        assertThrows(NotFoundException.class, () -> billService.findOne(-1L));
    }

    @Test
    public void findBillShouldWork() {
        Bill bill = new Bill(null, null, null, "TestBill", null, null, 10000, 0, LocalDate.now());
        billRepository.saveAndFlush(bill);
        assertNotNull(billService.findAll(null));

    }
}
