package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.BillRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class BillDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static Set<ItemStorage> GROCERIES;
    private static final String NOTES = "bought at billa";
    private static Set<ApplicationUser> NAMES;
    private static Set<ApplicationUser> NOT_PAID_NAMES;
    private static final double SUM = 48.0;
    private static final double SUM_PER_PERSON = 16.0;
    private static final LocalDate DATE = LocalDate.of(2021, 11, 3);

    private final ItemStorageRepository itemStorageRepository;
    private final UserRepository userRepository;
    private final BillRepository billRepository;

    private final UserDataGenerator userDataGenerator;

    public BillDataGenerator(ItemStorageRepository itemStorageRepository, UserRepository userRepository,
                             BillRepository billRepository,
                             UserDataGenerator userDataGenerator) {
        this.itemStorageRepository = itemStorageRepository;
        this.userRepository = userRepository;
        this.billRepository = billRepository;
        this.userDataGenerator = userDataGenerator;
    }

    @PostConstruct
    void generateRegister() {
        if (billRepository.findAll().size() > 0) {
            LOGGER.debug("bill already generated");
        } else {
            userDataGenerator.generateUser();

            //bills
            Bill bill = Bill.BillBuilder.aBill()
                .withRegisterId(1L)
                .withGroceries(GROCERIES)
                .withNotes("bought at Lidl")
                .withNames(NAMES)
                .withNotPaidNames(NOT_PAID_NAMES)
                .withSum(48)
                .withSumPerPerson(14)
                .withDate(DATE)
                .build();
            Bill savedBill = billRepository.saveAndFlush(bill);

            //user
            Optional<ApplicationUser> user = userRepository.findUserByUsername("user");
            Optional<ApplicationUser> admin = userRepository.findUserByUsername("admin");

            HashSet<ApplicationUser> names = new HashSet<>();
            user.ifPresent(names::add);
            admin.ifPresent(names::add);
            savedBill.setNames(names);
            savedBill = billRepository.saveAndFlush(savedBill);

            HashSet<ApplicationUser> notPaid = new HashSet<>();
            user.ifPresent(notPaid::add);
            admin.ifPresent(notPaid::add);
            savedBill.setNotPaidNames(notPaid);
            savedBill = billRepository.saveAndFlush(savedBill);

            /*
            ApplicationUser maleUser = new ApplicationUser("luke@email.com", "password");
            ApplicationUser user1 = userRepository.saveAndFlush(maleUser);
            ApplicationUser femaleUser = new ApplicationUser("anne@email.com", "password");
            ApplicationUser user2 = userRepository.saveAndFlush(femaleUser);

            savedBill.setNames(new HashSet<ApplicationUser>() {
                {
                    add(user1);
                    add(user2);
                }
            });

            savedBill = billRepository.saveAndFlush(savedBill);

            savedBill.setNotPaidNames(new HashSet<ApplicationUser>() {
                {
                    add(user1);
                    add(user2);
                }
            });
            savedBill = billRepository.saveAndFlush(savedBill);

             */



            //items
            ItemStorage itemStorage1 = new ItemStorage("name 1", "notes for itemStorage 1", null,
                null, 10, Location.fridge.toString(), null, null, null);
            ItemStorage item1 = itemStorageRepository.saveAndFlush(itemStorage1);

            savedBill.setGroceries(new HashSet<ItemStorage>() {
                {
                    add(item1);
                }
            });
            savedBill = billRepository.saveAndFlush(savedBill);


            // bill 2
            Bill bill2 = Bill.BillBuilder.aBill()
                .withRegisterId(1L)
                .withNotes("bought at Hofer")
                .withSum(24)
                .withSumPerPerson(12)
                .withDate(LocalDate.of(2021, 12, 17))
                .build();
            Bill savedBill2 = billRepository.saveAndFlush(bill2);

            savedBill2.setNames(names);
            savedBill2.setNotPaidNames(notPaid);

            ItemStorage itemStorage2 = new ItemStorage("name 2", "notes for itemStorage 2", null,
                null, 10, Location.fridge.toString(), null, null, null);
            ItemStorage item2 = itemStorageRepository.saveAndFlush(itemStorage2);
            savedBill2.setGroceries(new HashSet<ItemStorage>() {
                {
                    add(item2);
                }
            });
            billRepository.saveAndFlush(savedBill2);

            // bill 3
            Bill bill3 = Bill.BillBuilder.aBill()
                .withRegisterId(1L)
                .withNotes("bought at Spar")
                .withSum(32)
                .withSumPerPerson(16)
                .withDate(LocalDate.of(2021, 12, 10))
                .build();
            Bill savedBill3 = billRepository.saveAndFlush(bill3);

            savedBill3.setNames(names);
            savedBill3.setNotPaidNames(notPaid);

            ItemStorage itemStorage3 = new ItemStorage("name 3", "notes for itemStorage 3", null,
                null, 10, Location.fridge.toString(), null, null, null);
            ItemStorage item3 = itemStorageRepository.saveAndFlush(itemStorage3);
            savedBill3.setGroceries(new HashSet<ItemStorage>() {
                {
                    add(item3);
                }
            });
            billRepository.saveAndFlush(savedBill3);


        }
    }
}
