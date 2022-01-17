package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
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
    public void generateRegister() {
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

            /*

            Register register = Register.RegisterBuilder.aRegister()
                .withBills(TEST_BILLS)
                .withMonthlyPayment(TEST_MONTHLY_PAYMENT)
                .withMonthlyBudget(TEST_MONTHLY_BUDGET)
                .build();;
            Register savedRegister = registerRepository.saveAndFlush(register);





            //bills
            Bill bill2 = Bill.BillBuilder.aBill()
                .withRegisterId(savedRegister.getId())
                .withGroceries(GROCERIES)
                .withNotes("bought at billa")
                .withNames(NAMES)
                .withNotPaidNames(NOT_PAID_NAMES)
                .withSum(40)
                .withSumPerPerson(20)
                .withDate(DATE)
                .build();
            Bill savedBill2 = billRepository.saveAndFlush(bill2);
            TEST_BILLS.add(savedBill2);
            savedRegister.setBills(TEST_BILLS);
            savedRegister = registerRepository.saveAndFlush(savedRegister);



            //items
            ItemStorage itemStorage4 = new ItemStorage("name 4", "notes for itemStorage 1", null,
                null, 10, Location.fridge.toString(), null, null, null);
            ItemStorage item4 = itemStorageRepository.saveAndFlush(itemStorage4);
            ItemStorage itemStorage5 = new ItemStorage("name 5", "notes for itemStorage 2", null,
                null, 10, Location.fridge.toString(), null, null, null);
            ItemStorage item5 = itemStorageRepository.saveAndFlush(itemStorage5);
            ItemStorage itemStorage6 = new ItemStorage("name 6", "notes for itemStorage 3", null,
                null, 10, Location.fridge.toString(), null, null, null);
            ItemStorage item6 = itemStorageRepository.saveAndFlush(itemStorage6);

            savedBill2.setGroceries(new HashSet<ItemStorage>() {
                {
                    add(item4);
                    add(item5);
                    add(item6);
                }
            });
            savedBill2 = billRepository.saveAndFlush(savedBill2);
            savedRegister = registerRepository.saveAndFlush(savedRegister);


            Long shoppingListIdOfTom = shoppingListRepository.saveAndFlush(ShoppingList.ShoppingListBuilder.aShoppingList().withName("Your private shopping list").build()).getId();
            Long shoppingListIdOfLouise = shoppingListRepository.saveAndFlush(ShoppingList.ShoppingListBuilder.aShoppingList().withName("Your private shopping list").build()).getId();


            //user
            ApplicationUser maleUser = new ApplicationUser("tom", "password", shoppingListIdOfTom, "tom@email.com");
            ApplicationUser user3 = userRepository.saveAndFlush(maleUser);
            ApplicationUser femaleUser = new ApplicationUser("louise", "password", shoppingListIdOfLouise, "louise@email.com");
            ApplicationUser user4 = userRepository.saveAndFlush(femaleUser);

            savedBill2.setNames(new HashSet<ApplicationUser>() {
                {
                    add(user3);
                    add(user4);
                    add(userRepository.getById(2L));
                }
            });

            savedBill2 = billRepository.saveAndFlush(savedBill2);
            savedRegister = registerRepository.saveAndFlush(savedRegister);

            savedBill2.setNotPaidNames(new HashSet<ApplicationUser>() {
                {
                    add(user3);
                    add(user4);
                    add(userRepository.getById(2L));
                }
            });

            savedBill2 = billRepository.saveAndFlush(savedBill2);
            savedRegister = registerRepository.saveAndFlush(savedRegister);

            billList = billRepository.findAll();
            Bill finalSavedBill2 = savedBill2;
            HashSet<Bill> billSet = new HashSet<Bill>();
            billSet.add(finalSavedBill2);
            billSet.addAll(billList);
            savedRegister.setBills(billSet);

            registerRepository.saveAndFlush(savedRegister);

             */



        }
    }

}

