package at.ac.tuwien.sepm.groupphase.backend.datagenerator;


import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Profile("generateData")
@Component
public class ItemDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ItemRepository itemRepository;
    private static final String[] ITEM_NAMES_FOR_PIECES = {"Apple", "Orange", "Mango"};
    private static final String[] ITEM_NAMES_FOR_KG = {"Potato"};
    private static final String[] ITEM_NAMES_FOR_G = {"Sugar", "Salt"};
    private static final String[] ITEM_NAMES_FOR_L = {"Milk", "Cola"};

    private final UnitOfQuantityDataGenerator unitOfQuantityDataGenerator;
    private final UnitOfQuantityRepository unitOfQuantityRepository;

    public ItemDataGenerator(ItemRepository itemRepository,
                             UnitOfQuantityRepository unitOfQuantityRepository,
                             UnitOfQuantityDataGenerator unitOfQuantityDataGenerator) {
        this.unitOfQuantityDataGenerator = unitOfQuantityDataGenerator;
        this.itemRepository = itemRepository;
        this.unitOfQuantityRepository = unitOfQuantityRepository;
    }


    @PostConstruct
    void generateItem() {
        if(itemRepository.findAll().size() > 0) {
            LOGGER.debug("Item already generated");
        } else {
            LOGGER.debug("generating Item entries");

            this.unitOfQuantityDataGenerator.generateUnitOfQuantity();

            List<UnitOfQuantity> unitList = unitOfQuantityRepository.findAll();
            Map<String, Long> mappedUnits = new HashMap<>();
            for (UnitOfQuantity unit :
                unitList) {
                mappedUnits.put(unit.getName(), unit.getId());
            }

            for (String name :
                ITEM_NAMES_FOR_PIECES) {
                Item item = new Item(null, name, mappedUnits.get("pieces"));
                LOGGER.debug("saving item {}", item);
                itemRepository.save(item);
            }
            for (String name :
                ITEM_NAMES_FOR_KG) {
                Item item = new Item(null, name, mappedUnits.get("kg"));
                LOGGER.debug("saving Item {}", item);
                itemRepository.save(item);
            }
            for (String name :
                ITEM_NAMES_FOR_G) {
                Item item = new Item(null, name, mappedUnits.get("g"));
                LOGGER.debug("saving item {}", item);
                itemRepository.save(item);
            }
            for (String name :
                ITEM_NAMES_FOR_L) {
                Item item = new Item(null, name, mappedUnits.get("L"));
                LOGGER.debug("saving item {}", item);
                itemRepository.save(item);
            }
        }
    }


}
