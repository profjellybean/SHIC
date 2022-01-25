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
import java.io.IOException;
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
    private static final LocalDate DATE2 = LocalDate.of(2021, 11, 4);
    private static final LocalDate DATE3 = LocalDate.of(2021, 12, 17);

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
    void generateBills() throws IOException {
        if (billRepository.findAll().size() > 0) {
            LOGGER.debug("bill already generated");
        } else {
            userDataGenerator.generateUser();

            //bills
            Bill fbill = Bill.BillBuilder.aBill()
                .withId(1L)
                .withRegisterId(1L)
                .withGroceries(GROCERIES)
                .withNotes("bought at Lidl")
                .withNames(NAMES)
                .withNotPaidNames(NOT_PAID_NAMES)
                .withSum(10)
                .withSumPerPerson(5)
                .withDate(DATE1)
                .build();
            Bill fsavedBill = billRepository.saveAndFlush(fbill);

            //user
            Optional<ApplicationUser> user = userRepository.findUserByUsername("Leopold");
            Optional<ApplicationUser> admin = userRepository.findUserByUsername("Heidi");

            HashSet<ApplicationUser> fnames = new HashSet<>();
            user.ifPresent(fnames::add);
            admin.ifPresent(fnames::add);
            fsavedBill.setNames(fnames);
            fsavedBill = billRepository.saveAndFlush(fsavedBill);

            HashSet<ApplicationUser> fnotPaid = new HashSet<>();
            user.ifPresent(fnotPaid::add);
            admin.ifPresent(fnotPaid::add);
            fsavedBill.setNotPaidNames(fnotPaid);
            fsavedBill = billRepository.saveAndFlush(fsavedBill);

            Bill fbill2 = Bill.BillBuilder.aBill()
                .withId(2L)
                .withRegisterId(1L)
                .withGroceries(GROCERIES)
                .withNotes("bought at Spar")
                .withNames(NAMES)
                .withNotPaidNames(NOT_PAID_NAMES)
                .withSum(20)
                .withSumPerPerson(10)
                .withDate(DATE2)
                .build();
            Bill fsavedBill2 = billRepository.saveAndFlush(fbill2);

            //user
            Optional<ApplicationUser> fuser2 = userRepository.findUserByUsername("Leopold");
            Optional<ApplicationUser> fadmin2 = userRepository.findUserByUsername("Heidi");

            HashSet<ApplicationUser> fnames2 = new HashSet<>();
            fuser2.ifPresent(fnames2::add);
            fadmin2.ifPresent(fnames2::add);
            fsavedBill2.setNames(fnames2);
            fsavedBill2 = billRepository.saveAndFlush(fsavedBill2);

            HashSet<ApplicationUser> fnotPaid2 = new HashSet<>();
            fuser2.ifPresent(fnotPaid2::add);
            fadmin2.ifPresent(fnotPaid2::add);
            fsavedBill2.setNotPaidNames(fnotPaid2);
            fsavedBill2 = billRepository.saveAndFlush(fsavedBill2);

            Bill fbill3 = Bill.BillBuilder.aBill()
                .withId(2L)
                .withRegisterId(1L)
                .withGroceries(GROCERIES)
                .withNotes("bought at Hofer")
                .withNames(NAMES)
                .withNotPaidNames(NOT_PAID_NAMES)
                .withSum(30)
                .withSumPerPerson(15)
                .withDate(DATE3)
                .build();
            Bill fsavedBill3 = billRepository.saveAndFlush(fbill3);

            //user
            Optional<ApplicationUser> fuser3 = userRepository.findUserByUsername("Leopold");
            Optional<ApplicationUser> fadmin3 = userRepository.findUserByUsername("Heidi");

            HashSet<ApplicationUser> fnames3 = new HashSet<>();
            fuser3.ifPresent(fnames3::add);
            fadmin3.ifPresent(fnames3::add);
            fsavedBill3.setNames(fnames3);
            fsavedBill3 = billRepository.saveAndFlush(fsavedBill3);

            HashSet<ApplicationUser> fnotPaid3 = new HashSet<>();
            fuser3.ifPresent(fnotPaid3::add);
            fadmin3.ifPresent(fnotPaid3::add);
            fsavedBill3.setNotPaidNames(fnotPaid3);
            fsavedBill3 = billRepository.saveAndFlush(fsavedBill3);

            Bill fbill4 = Bill.BillBuilder.aBill()
                .withId(5L)
                .withRegisterId(1L)
                .withGroceries(GROCERIES)
                .withNotes("bought at Billa")
                .withNames(NAMES)
                .withNotPaidNames(NOT_PAID_NAMES)
                .withSum(30)
                .withSumPerPerson(15)
                .withDate(LocalDate.of(2022, 1, 25))
                .build();
            Bill fsavedBill4 = billRepository.saveAndFlush(fbill4);

            //user
            Optional<ApplicationUser> fuser4 = userRepository.findUserByUsername("Leopold");
            Optional<ApplicationUser> fadmin4 = userRepository.findUserByUsername("Heidi");

            HashSet<ApplicationUser> fnames4 = new HashSet<>();
            fuser4.ifPresent(fnames4::add);
            fadmin4.ifPresent(fnames4::add);
            fsavedBill4.setNames(fnames4);
            fsavedBill4 = billRepository.saveAndFlush(fsavedBill4);

            HashSet<ApplicationUser> fnotPaid4 = new HashSet<>();
            fuser4.ifPresent(fnotPaid4::add);
            fadmin4.ifPresent(fnotPaid4::add);
            fsavedBill4.setNotPaidNames(fnotPaid4);
            fsavedBill4 = billRepository.saveAndFlush(fsavedBill4);

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

        }
    }
}
