package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
import at.ac.tuwien.sepm.groupphase.backend.repository.RegisterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Profile("generateData")
@Component
public class RegisterDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_REGISTERS_TO_GENERATE = 3;
    private static final Set<Bill> TEST_BILLS = new HashSet<Bill>();
    private static final double TEST_MONTHLY_PAYMENT = 300;
    private static final double TEST_MONTHLY_BUDGET = 500;

    private final RegisterRepository registerRepository;

    public RegisterDataGenerator(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    @PostConstruct
    private void generateRegister() {
        if (registerRepository.findAll().size() > 0) {
            LOGGER.debug("register already generated");
        } else {
            LOGGER.debug("generating {} message entries", NUMBER_OF_REGISTERS_TO_GENERATE);
            TEST_BILLS.add(null);
            for (int i = 0; i < NUMBER_OF_REGISTERS_TO_GENERATE; i++) {
                Register register = Register.RegisterBuilder.aRegister()
                    .withBills(TEST_BILLS)
                    .withMonthlyPayment(TEST_MONTHLY_PAYMENT + i)
                    .withMonthlyBudget(TEST_MONTHLY_BUDGET + i)
                    .build();
                LOGGER.debug("saving register {}", register);
                registerRepository.save(register);
                System.out.println(register.getId());
            }
        }
    }

}

