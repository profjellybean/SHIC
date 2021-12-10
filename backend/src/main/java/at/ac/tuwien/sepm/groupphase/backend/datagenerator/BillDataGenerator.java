package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.repository.BillRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.Set;

public class BillDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static Set<ItemStorage> GROCERIES;
    private static final String NOTES = "bought at billa";
    private static Set<ApplicationUser> NAMES;
    private static Set<ApplicationUser> NOT_PAID_NAMES;
    private static final double SUM = 48.0;
    private static final double SUM_PER_PERSON = 16.0;
    private static final LocalDate DATE = LocalDate.of(2021, 12, 3);

    private final ItemStorageRepository itemStorageRepository;
    private final UserRepository userRepository;
    private final BillRepository billRepository;

    public BillDataGenerator(ItemStorageRepository itemStorageRepository, UserRepository userRepository, BillRepository billRepository) {
        this.itemStorageRepository = itemStorageRepository;
        this.userRepository = userRepository;
        this.billRepository = billRepository;
    }

    @PostConstruct
    private void generateRegister() {
        if (billRepository.findAll().size() > 0) {
            LOGGER.debug("bill already generated");
        } else {
            /*
            //bills
            Bill bill = Bill.BillBuilder.aBill()
                .withRegisterId(1L)
                .withGroceries(GROCERIES)
                .withNotes("bought at billa")
                .withNames(NAMES)
                .withNotPaidNames(NOT_PAID_NAMES)
                .withSum(48)
                .withSumPerPerson(14)
                .withDate(DATE)
                .build();
            Bill savedBill = billRepository.saveAndFlush(bill);

            //user
            ApplicationUser maleUser = new ApplicationUser("luke@email.com", "password");
            ApplicationUser user1 = userRepository.saveAndFlush(maleUser);
            ApplicationUser femaleUser = new ApplicationUser("anne@email.com", "password");
            ApplicationUser user2 = userRepository.saveAndFlush(femaleUser);

            savedBill.setNames(new HashSet<ApplicationUser>() {{
                add(user1);
                add(user2);
            }});

            savedBill = billRepository.saveAndFlush(savedBill);

            savedBill.setNotPaidNames(new HashSet<ApplicationUser>() {{
                add(user1);
                add(user2);
            }});

            savedBill = billRepository.saveAndFlush(savedBill);


            //items
            ItemStorage itemStorage1 = new ItemStorage("name 1", "notes for itemStorage 1", null,
                null, 10, Location.fridge, UnitOfQuantity.kg, null);
            ItemStorage item1 = itemStorageRepository.saveAndFlush(itemStorage1);
            ItemStorage itemStorage2 = new ItemStorage("name 2", "notes for itemStorage 2", null,
                null, 10, Location.fridge, UnitOfQuantity.kg, null);
            ItemStorage item2 = itemStorageRepository.saveAndFlush(itemStorage2);
            ItemStorage itemStorage3 = new ItemStorage("name 3", "notes for itemStorage 3", null,
                null, 10, Location.fridge, UnitOfQuantity.kg, null);
            ItemStorage item3 = itemStorageRepository.saveAndFlush(itemStorage3);

            savedBill.setGroceries(new HashSet<ItemStorage>(){{
                add(item1);
                add(item2);
                add(item3);
            }});
            savedBill = billRepository.saveAndFlush(savedBill);

             */


        }
    }
}
