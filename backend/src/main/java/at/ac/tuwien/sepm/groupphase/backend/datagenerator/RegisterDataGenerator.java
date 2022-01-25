package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
import at.ac.tuwien.sepm.groupphase.backend.repository.BillRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RegisterRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Profile("generateData")
@Component
public class RegisterDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_REGISTERS_TO_GENERATE = 3;
    private static final double TEST_MONTHLY_PAYMENT = 300;
    private static final double TEST_MONTHLY_BUDGET = 500;
    private static final String NOTES = "bought at billa";
    private static final double SUM = 48.0;
    private static final double SUM_PER_PERSON = 16.0;
    private static final LocalDate DATE = LocalDate.of(2021, 12, 3);
    private static Set<Bill> TEST_BILLS;
    private static Set<ItemStorage> GROCERIES;
    private static Set<ApplicationUser> NAMES;
    private static Set<ApplicationUser> NOT_PAID_NAMES;
    private final ItemStorageRepository itemStorageRepository;
    private final UserRepository userRepository;
    private final BillRepository billRepository;
    private final RegisterRepository registerRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final BillDataGenerator billDataGenerator;

    public RegisterDataGenerator(ItemStorageRepository itemStorageRepository, UserRepository userRepository,
                                 BillRepository billRepository, RegisterRepository registerRepository,
                                 ShoppingListRepository shoppingListRepository,
                                 BillDataGenerator billDataGenerator) {
        this.itemStorageRepository = itemStorageRepository;
        this.userRepository = userRepository;
        this.billRepository = billRepository;
        this.registerRepository = registerRepository;
        this.shoppingListRepository = shoppingListRepository;
        this.billDataGenerator = billDataGenerator;
    }

    @PostConstruct
    public void generateRegister() throws IOException {
        if (registerRepository.findAll().size() > 0) {
            LOGGER.debug("register already generated");
        } else {
            billDataGenerator.generateBills();

            List<Bill> billList = billRepository.findAll();

            TEST_BILLS = new HashSet<>(billList);

            //register
            Optional<Register> registerOfGroup = registerRepository.findRegisterById(1L);
            Register register;
            if (registerOfGroup.isPresent()) {
                register = registerOfGroup.get();
                register.setBills(TEST_BILLS);
                register.setMonthlyPayments(TEST_MONTHLY_PAYMENT);
                register.setMonthlyBudget(TEST_MONTHLY_BUDGET);
            } else {
                register = Register.RegisterBuilder.aRegister()
                    .withBills(TEST_BILLS)
                    .withMonthlyPayment(TEST_MONTHLY_PAYMENT)
                    .withMonthlyBudget(TEST_MONTHLY_BUDGET)
                    .build();
            }
            Register savedRegister = registerRepository.saveAndFlush(register);
        }
    }

}

