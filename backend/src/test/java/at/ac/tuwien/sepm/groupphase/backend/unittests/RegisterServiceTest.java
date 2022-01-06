package at.ac.tuwien.sepm.groupphase.backend.unittests;


import at.ac.tuwien.sepm.groupphase.backend.datagenerator.TestDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.RegisterRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.RegisterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class RegisterServiceTest {

    @Autowired
    RegisterService registerService;

    @Autowired
    RegisterRepository registerRepository;

    @Autowired
    TestDataGenerator testDataGenerator;

    @Autowired
    PlatformTransactionManager txm;

    TransactionStatus txstatus;

    @BeforeEach
    public void setupDBTransaction() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        txstatus = txm.getTransaction(def);
        assumeTrue(txstatus.isNewTransaction());
        txstatus.setRollbackOnly();
    }

    @AfterEach
    public void tearDownDBData() {
        txm.rollback(txstatus);
    }


    @Test
    public void givenNoUsernameWhen_editMonthlyBudget_thenValidationException() throws Exception {
        assertThrows(ValidationException.class, () -> registerService.editMonthlyBudget(null, 1.0));
    }

    @Test
    public void givenNoBudgetWhen_editMonthlyBudget_thenValidationException() throws Exception {
        testDataGenerator.generateData_editMonthlyBudget_inRegister();

        assertThrows(ValidationException.class, () -> registerService.editMonthlyBudget("testUser", null));
    }

    @Test
    public void givenNegativeBudgetWhen_editMonthlyBudget_thenValidationException() throws Exception {
        testDataGenerator.generateData_editMonthlyBudget_inRegister();

        assertThrows(ValidationException.class, () -> registerService.editMonthlyBudget("testUser", -100.0));
    }

    @Test
    public void givenNoRegisterWhen_editMonthlyBudget_thenNotFoundException() throws Exception {
        testDataGenerator.generateData_generateUser_withGroup_withOnlyNullValues();

        assertThrows(NotFoundException.class, () -> registerService.editMonthlyBudget("testUser", 100.0));
    }

    @Test
    public void editMonthlyBudget_shouldNotThrowException() throws Exception {
        testDataGenerator.generateData_editMonthlyBudget_inRegister();

        assertDoesNotThrow(() -> registerService.editMonthlyBudget("testUser", 100.0));
    }

    @Test
    public void editMonthlyBudget_shouldWorkAndNotThrowException() throws Exception {
        Long registerId = testDataGenerator.generateData_editMonthlyBudget_inRegister();
        double newBudget = 100.0;

        assertDoesNotThrow(() -> registerService.editMonthlyBudget(
            "testUser", newBudget));
        Register editedRegister = registerRepository.getById(registerId);
        assertEquals(newBudget, editedRegister.getMonthlyBudget());

    }
}
