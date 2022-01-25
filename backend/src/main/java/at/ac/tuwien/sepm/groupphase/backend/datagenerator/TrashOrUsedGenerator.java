package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.TrashOrUsed;
import at.ac.tuwien.sepm.groupphase.backend.entity.TrashOrUsedItem;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TrashOrUsedItemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TrashOrUsedRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class TrashOrUsedGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final TrashOrUsedRepository trashOrUsedRepository;
    private final UserRepository userRepository;
    private final ItemStorageDataGenerator itemStorageDataGenerator;
    private final ItemStorageRepository itemStorageRepository;
    private final TrashOrUsedItemRepository trashOrUsedItemRepository;
    private final UnitOfQuantityRepository unitOfQuantityRepository;

    private final UnitOfQuantityDataGenerator unitOfQuantityDataGenerator;


    public TrashOrUsedGenerator(TrashOrUsedRepository trashOrUsedRepository, UserRepository userRepository,
                                ItemStorageDataGenerator itemStorageDataGenerator, ItemStorageRepository itemStorageRepository,
                                TrashOrUsedItemRepository trashOrUsedItemRepository, UserDataGenerator userDataGenerator, UnitOfQuantityRepository unitOfQuantityRepository, UnitOfQuantityDataGenerator unitOfQuantityDataGenerator) {
        this.trashOrUsedRepository = trashOrUsedRepository;
        this.userRepository = userRepository;
        this.itemStorageDataGenerator = itemStorageDataGenerator;
        this.itemStorageRepository = itemStorageRepository;
        this.trashOrUsedItemRepository = trashOrUsedItemRepository;
        this.unitOfQuantityRepository = unitOfQuantityRepository;


        this.unitOfQuantityDataGenerator = unitOfQuantityDataGenerator;
    }

    @PostConstruct
    void generate() throws IOException, ParseException {

        this.unitOfQuantityDataGenerator.generateUnitOfQuantity();
        List<UnitOfQuantity> unitList = unitOfQuantityRepository.findAll();

        if (trashOrUsedRepository.findAll().size() > 0 && trashOrUsedItemRepository.findAll().size() > 0) {
            LOGGER.debug("trash or used already generated");
        } else {

            TrashOrUsedItem item = new TrashOrUsedItem(11, "Feta", 6L);
            trashOrUsedItemRepository.saveAndFlush(item);
            TrashOrUsedItem item1 = new TrashOrUsedItem(9, "Mango", 6L);
            trashOrUsedItemRepository.saveAndFlush(item1);
            TrashOrUsedItem item2 = new TrashOrUsedItem(9, "Milk", 6L);
            trashOrUsedItemRepository.saveAndFlush(item2);
            TrashOrUsedItem item3 = new TrashOrUsedItem(8, "Mozzarella", 6L);
            trashOrUsedItemRepository.saveAndFlush(item3);
            TrashOrUsedItem item4 = new TrashOrUsedItem(6, "Chicken", 6L);
            trashOrUsedItemRepository.saveAndFlush(item4);
            TrashOrUsedItem item5 = new TrashOrUsedItem(5, "Yoghurt", 6L);
            trashOrUsedItemRepository.saveAndFlush(item5);
            TrashOrUsedItem item6 = new TrashOrUsedItem(3, "Whipped Cream", 6L);
            trashOrUsedItemRepository.saveAndFlush(item6);


            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date date = format.parse("01-01-2019");


            for (int i = 0; i < 44; i++) {
                TrashOrUsed trashOrUsed = new TrashOrUsed(date, 4, 6L, unitList.get(0), "test" + i + "2019");
                trashOrUsedRepository.saveAndFlush(trashOrUsed);
            }
            date = format.parse("01-01-2020");
            for (int i = 0; i < 27; i++) {
                TrashOrUsed trashOrUsed = new TrashOrUsed(date, 4, 6L, unitList.get(0), "test" + i + "2020");
                trashOrUsedRepository.saveAndFlush(trashOrUsed);
            }

            date = format.parse("01-02-2021");
            for (int i = 0; i < 3; i++) {
                TrashOrUsed trashOrUsed = new TrashOrUsed(date, 4, 6L, unitList.get(0), "test" + i + "Februar");
                trashOrUsedRepository.saveAndFlush(trashOrUsed);
            }

            date = format.parse("01-03-2021");
            for (int i = 0; i < 7; i++) {
                TrashOrUsed trashOrUsed = new TrashOrUsed(date, 4, 6L, unitList.get(0), "test" + i + "März");
                trashOrUsedRepository.saveAndFlush(trashOrUsed);
            }
            date = format.parse("01-04-2021");
            for (int i = 0; i < 5; i++) {
                TrashOrUsed trashOrUsed = new TrashOrUsed(date, 4, 6L, unitList.get(0), "test" + i + "April");
                trashOrUsedRepository.saveAndFlush(trashOrUsed);
            }
            date = format.parse("01-05-2021");
            for (int i = 0; i < 3; i++) {
                TrashOrUsed trashOrUsed = new TrashOrUsed(date, 4, 6L, unitList.get(0), "test" + i + "Mai");
                trashOrUsedRepository.saveAndFlush(trashOrUsed);
            }
            date = format.parse("01-06-2021");
            for (int i = 0; i < 7; i++) {
                TrashOrUsed trashOrUsed = new TrashOrUsed(date, 4, 6L, unitList.get(0), "test" + i + "Juni");
                trashOrUsedRepository.saveAndFlush(trashOrUsed);
            }
            date = format.parse("01-07-2021");
            for (int i = 0; i < 3; i++) {
                TrashOrUsed trashOrUsed = new TrashOrUsed(date, 4, 6L, unitList.get(0), "test" + i + "Juli");
                trashOrUsedRepository.saveAndFlush(trashOrUsed);
            }
            date = format.parse("01-08-2021");
            for (int i = 0; i < 5; i++) {
                TrashOrUsed trashOrUsed = new TrashOrUsed(date, 4, 6L, unitList.get(0), "test" + i + "August");
                trashOrUsedRepository.saveAndFlush(trashOrUsed);
            }
            date = format.parse("01-09-2021");
            for (int i = 0; i < 1; i++) {
                TrashOrUsed trashOrUsed = new TrashOrUsed(date, 4, 6L, unitList.get(0), "test" + i + "September");
                trashOrUsedRepository.saveAndFlush(trashOrUsed);
            }
            date = format.parse("01-10-2021");
            for (int i = 0; i < 9; i++) {
                TrashOrUsed trashOrUsed = new TrashOrUsed(date, 4, 6L, unitList.get(0), "test" + i + "Oktober");
                trashOrUsedRepository.saveAndFlush(trashOrUsed);
            }
            date = format.parse("01-11-2021");
            for (int i = 0; i < 4; i++) {
                TrashOrUsed trashOrUsed = new TrashOrUsed(date, 4, 6L, unitList.get(0), "test" + i + "November");
                trashOrUsedRepository.saveAndFlush(trashOrUsed);
            }
            date = format.parse("01-12-2021");
            for (int i = 0; i < 4; i++) {
                TrashOrUsed trashOrUsed = new TrashOrUsed(date, 4, 6L, unitList.get(0), "test" + i + "Dezember");
                trashOrUsedRepository.saveAndFlush(trashOrUsed);
            }
            date = format.parse("01-1-2022");
            for (int i = 0; i < 6; i++) {
                TrashOrUsed trashOrUsed = new TrashOrUsed(date, 4, 6L, unitList.get(0), "test" + i + "Jänner");
                trashOrUsedRepository.saveAndFlush(trashOrUsed);
            }


        }


    }
}

