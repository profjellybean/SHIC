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
            Optional<ApplicationUser> fuser = userRepository.findUserByUsername("Leopold");
            Optional<ApplicationUser> fadmin = userRepository.findUserByUsername("Heidi");

            HashSet<ApplicationUser> fnames = new HashSet<>();
            fuser.ifPresent(fnames::add);
            fadmin.ifPresent(fnames::add);
            fsavedBill.setNames(fnames);
            fsavedBill = billRepository.saveAndFlush(fsavedBill);

            HashSet<ApplicationUser> fnotPaid = new HashSet<>();
            fuser.ifPresent(fnotPaid::add);
            fadmin.ifPresent(fnotPaid::add);
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

        }
    }
}
