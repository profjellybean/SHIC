package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.BillRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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
    private static final LocalDate DATE1 = LocalDate.of(2021, 11, 3);


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
                    .withDate(DATE1)
                    .build();
                if (i == 2) {
                    bill.setDate(LocalDate.now());
                }
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
            Bill bill = Bill.BillBuilder.aBill()
                .withId(5L)
                .withRegisterId(1L)
                .withGroceries(GROCERIES)
                .withNotes("bought at Billa")
                .withNames(NAMES)
                .withNotPaidNames(NOT_PAID_NAMES)
                .withSum(30)
                .withSumPerPerson(15)
                .withDate(LocalDate.of(2021, 11, 15))
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


            Bill bill1 = Bill.BillBuilder.aBill()
                .withId(6L)
                .withRegisterId(1L)
                .withGroceries(GROCERIES)
                .withNotes("bought at Billa")
                .withNames(NAMES)
                .withNotPaidNames(NOT_PAID_NAMES)
                .withSum(4830)
                .withSumPerPerson(15)
                .withDate(LocalDate.of(2019, 11, 15))
                .build();
            Bill savedBill1 = billRepository.saveAndFlush(bill1);
            HashSet<ApplicationUser> names1 = new HashSet<>();
            user.ifPresent(names1::add);
            admin.ifPresent(names1::add);
            savedBill1.setNames(names1);
            savedBill1 = billRepository.saveAndFlush(savedBill1);

            HashSet<ApplicationUser> notPaid1 = new HashSet<>();
            savedBill1.setNotPaidNames(notPaid1);
            savedBill1 = billRepository.saveAndFlush(savedBill1);

            Bill bill2 = Bill.BillBuilder.aBill()
                .withId(7L)
                .withRegisterId(1L)
                .withSum(3085)
                .withDate(LocalDate.of(2020, 11, 15))
                .build();
            Bill savedBill2 = billRepository.saveAndFlush(bill2);
            savedBill2.setNames(names1);
            savedBill2 = billRepository.saveAndFlush(savedBill2);

            Bill bill3 = Bill.BillBuilder.aBill()
                .withId(8L)
                .withRegisterId(1L)
                .withSum(485)
                .withDate(LocalDate.of(2021, 2, 15))
                .build();
            Bill savedBill3 = billRepository.saveAndFlush(bill3);
            savedBill3.setNames(names1);
            savedBill3 = billRepository.saveAndFlush(savedBill3);

            Bill bill4 = Bill.BillBuilder.aBill()
                .withId(9L)
                .withRegisterId(1L)
                .withSum(512)
                .withDate(LocalDate.of(2021, 3, 15))
                .build();
            Bill savedBill4 = billRepository.saveAndFlush(bill4);
            savedBill4.setNames(names1);
            savedBill4 = billRepository.saveAndFlush(savedBill4);

            Bill bill5 = Bill.BillBuilder.aBill()
                .withId(10L)
                .withRegisterId(1L)
                .withSum(308)
                .withDate(LocalDate.of(2021, 4, 15))
                .build();
            Bill savedBill5 = billRepository.saveAndFlush(bill5);
            savedBill5.setNames(names1);
            savedBill5 = billRepository.saveAndFlush(savedBill5);

            Bill bill6 = Bill.BillBuilder.aBill()
                .withId(11L)
                .withRegisterId(1L)
                .withSum(223)
                .withDate(LocalDate.of(2021, 5, 15))
                .build();
            Bill savedBill6 = billRepository.saveAndFlush(bill6);
            savedBill6.setNames(names1);
            savedBill6 = billRepository.saveAndFlush(savedBill6);

            Bill bill7 = Bill.BillBuilder.aBill()
                .withId(12L)
                .withRegisterId(1L)
                .withSum(423)
                .withDate(LocalDate.of(2021, 6, 15))
                .build();
            Bill savedBill7 = billRepository.saveAndFlush(bill7);
            savedBill7.setNames(names1);
            savedBill7 = billRepository.saveAndFlush(savedBill7);

            Bill bill8 = Bill.BillBuilder.aBill()
                .withId(13L)
                .withRegisterId(1L)
                .withSum(423)
                .withDate(LocalDate.of(2021, 7, 15))
                .build();
            Bill savedBill8 = billRepository.saveAndFlush(bill8);
            savedBill8.setNames(names1);
            savedBill8 = billRepository.saveAndFlush(savedBill8);

            Bill bill9 = Bill.BillBuilder.aBill()
                .withId(14L)
                .withRegisterId(1L)
                .withSum(178)
                .withDate(LocalDate.of(2021, 8, 15))
                .build();
            Bill savedBill9 = billRepository.saveAndFlush(bill9);
            savedBill9.setNames(names1);
            savedBill9 = billRepository.saveAndFlush(savedBill9);

            Bill bill10 = Bill.BillBuilder.aBill()
                .withId(15L)
                .withRegisterId(1L)
                .withSum(423)
                .withDate(LocalDate.of(2021, 9, 15))
                .build();
            Bill savedBill10 = billRepository.saveAndFlush(bill10);
            savedBill10.setNames(names1);
            savedBill10 = billRepository.saveAndFlush(savedBill10);

            Bill bill11 = Bill.BillBuilder.aBill()
                .withId(16L)
                .withRegisterId(1L)
                .withSum(423)
                .withDate(LocalDate.of(2021, 10, 15))
                .build();
            Bill savedBill11 = billRepository.saveAndFlush(bill11);
            savedBill11.setNames(names1);
            savedBill11 = billRepository.saveAndFlush(savedBill11);

            Bill bill12 = Bill.BillBuilder.aBill()
                .withId(17L)
                .withRegisterId(1L)
                .withSum(152)
                .withDate(LocalDate.of(2021, 11, 14))
                .build();
            Bill savedBill12 = billRepository.saveAndFlush(bill12);
            savedBill12.setNames(names1);
            savedBill12 = billRepository.saveAndFlush(savedBill12);

            Bill bill13 = Bill.BillBuilder.aBill()
                .withId(18L)
                .withRegisterId(1L)
                .withSum(314)
                .withDate(LocalDate.of(2021, 12, 15))
                .build();
            Bill savedBill13 = billRepository.saveAndFlush(bill13);
            savedBill13.setNames(names1);
            savedBill13 = billRepository.saveAndFlush(savedBill13);

            Bill bill14 = Bill.BillBuilder.aBill()
                .withId(19L)
                .withRegisterId(1L)
                .withSum(40)
                .withDate(LocalDate.of(2022, 1, 1))
                .build();
            Bill savedBill14 = billRepository.saveAndFlush(bill14);
            savedBill14.setNames(names1);
            savedBill14 = billRepository.saveAndFlush(savedBill14);
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
