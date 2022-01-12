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
    void generateBills() {
        if (billRepository.findAll().size() > 0) {
            LOGGER.debug("bill already generated");
        } else {
            userDataGenerator.generateUser();

            //bills
            for (int i = 0; i < 3; i++) {
                Bill bill = Bill.BillBuilder.aBill()
                    .withId(1L + i)
                    .withRegisterId(1L)
                    .withGroceries(GROCERIES)
                    .withNotes("bought at Lidl")
                    .withNames(NAMES)
                    .withNotPaidNames(NOT_PAID_NAMES)
                    .withSum(10 * (i + 1))
                    .withSumPerPerson((10 * (i + 1)) / 2)
                    .withDate(DATE)
                    .build();
                Bill savedBill = billRepository.saveAndFlush(bill);

                //user
                Optional<ApplicationUser> user = userRepository.findUserByUsername("Leopold");
                Optional<ApplicationUser> admin = userRepository.findUserByUsername("Heidi");

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

            }

            /*
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


            HashSet<Bill> bills = new HashSet<>();
            bills.add(savedBill);


            // bill 2
            Bill bill2 = Bill.BillBuilder.aBill()
                .withRegisterId(1L)
                .withNotes("bought at Hofer")
                .withSum(24)
                .withSumPerPerson(12)
                .withDate(LocalDate.of(2021, 12, 17))
                .build();
            Bill savedBill2 = billRepository.saveAndFlush(bill2);

            HashSet<ApplicationUser> names2 = new HashSet<>();
            user.ifPresent(names2::add);
            admin.ifPresent(names2::add);
            savedBill2.setNames(names2);
            savedBill2 = billRepository.saveAndFlush(savedBill2);

            HashSet<ApplicationUser> notPaid2 = new HashSet<>();
            user.ifPresent(notPaid2::add);
            admin.ifPresent(notPaid2::add);
            savedBill2.setNotPaidNames(notPaid2);
            savedBill2 = billRepository.saveAndFlush(savedBill2);


            ItemStorage itemStorage2 = new ItemStorage("name 2", "notes for itemStorage 2", null,
                null, 10, Location.fridge.toString(), null, null, null);
            ItemStorage item2 = itemStorageRepository.saveAndFlush(itemStorage2);
            savedBill2.setGroceries(new HashSet<ItemStorage>() {
                {
                    add(item2);
                }
            });
            savedBill2 = billRepository.saveAndFlush(savedBill2);


            bills.add(savedBill2);

            // bill 3
            Bill bill3 = Bill.BillBuilder.aBill()
                .withRegisterId(1L)
                .withNotes("bought at Spar")
                .withSum(32)
                .withSumPerPerson(16)
                .withDate(LocalDate.of(2021, 12, 10))
                .build();
            Bill savedBill3 = billRepository.saveAndFlush(bill3);

            HashSet<ApplicationUser> names3 = new HashSet<>();
            user.ifPresent(names3::add);
            admin.ifPresent(names3::add);
            savedBill3.setNames(names3);
            savedBill3 = billRepository.saveAndFlush(savedBill3);

            HashSet<ApplicationUser> notPaid3 = new HashSet<>();
            user.ifPresent(notPaid3::add);
            admin.ifPresent(notPaid3::add);
            savedBill3.setNotPaidNames(notPaid3);
            savedBill3 = billRepository.saveAndFlush(savedBill3);

            /*
            ItemStorage itemStorage3 = new ItemStorage("name 3", "notes for itemStorage 3", null,
                null, 10, Location.fridge.toString(), null, null, null);
            ItemStorage item3 = itemStorageRepository.saveAndFlush(itemStorage3);
            savedBill3.setGroceries(new HashSet<ItemStorage>() {
                {
                    add(item3);
                }
            });
            savedBill3 = billRepository.saveAndFlush(savedBill3);


            bills.add(savedBill3);
            /*
            if (admin.isPresent()) {
                ApplicationUser applicationAdmin = admin.get();
                applicationAdmin.setBills(bills);
                userRepository.saveAndFlush(applicationAdmin);
            }

            if (user.isPresent()) {
                ApplicationUser applicationUser = user.get();
                applicationUser.setBills(bills);
                userRepository.saveAndFlush(applicationUser);
            }

            */


        }
    }

}
