package at.ac.tuwien.sepm.groupphase.backend.datagenerator;


import at.ac.tuwien.sepm.groupphase.backend.entity.Item;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;

//@Profile("generateData")
@Component
public class ItemDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ItemRepository itemRepository;
    private static final String[] ITEM_NAMES_FOR_PIECES = {"Apple", "Orange", "Mango"};
    private static final String[] ITEM_NAMES_FOR_KG = {"Potato"};
    private static final String[] ITEM_NAMES_FOR_G = {"Sugar", "Salt"};
    private static final String[] ITEM_NAMES_FOR_L = {"Milk", "Cola"};

    public ItemDataGenerator(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    @PostConstruct
    void generateItem() {
        if(itemRepository.findAll().size() > 0) {
            LOGGER.debug("Item already generated");
        } else {
            LOGGER.debug("generating Item entries");

            for (String name :
                ITEM_NAMES_FOR_PIECES) {
                Item item = new Item(null, name, UnitOfQuantity.pieces);
                LOGGER.debug("saving item {}", item);
                itemRepository.save(item);
            }
            for (String name :
                ITEM_NAMES_FOR_KG) {
                Item item = new Item(null, name, UnitOfQuantity.kg);
                LOGGER.debug("saving Item {}", item);
                itemRepository.save(item);
            }
            for (String name :
                ITEM_NAMES_FOR_G) {
                Item item = new Item(null, name, UnitOfQuantity.g);
                LOGGER.debug("saving item {}", item);
                itemRepository.save(item);
            }
            for (String name :
                ITEM_NAMES_FOR_L) {
                Item item = new Item(null, name, UnitOfQuantity.L);
                LOGGER.debug("saving item {}", item);
                itemRepository.save(item);
            }
        }
    }


}
