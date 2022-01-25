package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitsRelation;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitsRelationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

//@Profile("generateData")
@Component
public class UnitOfQuantityDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UnitOfQuantityRepository unitOfQuantityRepository;
    private final UnitsRelationRepository unitsRelationRepository;
    private final ItemService itemService;
    private static final String[] UNITS = {"kg", "g", "L", "ml", "pieces", "can", "cup", "jar", "dag"};
    private static final UnitsRelation unitsRelation = new UnitsRelation("kg", "g", 1000.0);
    private static final UnitsRelation unitsRelation2 = new UnitsRelation("L", "ml", 1000.0);
    private static final UnitsRelation unitsRelation3 = new UnitsRelation("kg", "dag", 100.0);
    private static final UnitsRelation unitsRelation4 = new UnitsRelation("g", "dag", 0.1);

    public UnitOfQuantityDataGenerator(UnitOfQuantityRepository unitOfQuantityRepository, UnitsRelationRepository unitsRelationRepository, ItemService itemService) {
        this.unitOfQuantityRepository = unitOfQuantityRepository;
        this.unitsRelationRepository = unitsRelationRepository;
        this.itemService = itemService;
    }

    //@PostConstruct
    void generateUnitOfQuantity() {
        if (unitOfQuantityRepository.findAll().size() > 0) {
            LOGGER.debug("UnitOfQuantity already generated");
        } else {
            for (String unit :
                UNITS) {
                UnitOfQuantity unitOfQuantity = new UnitOfQuantity(unit);
                unitOfQuantityRepository.save(unitOfQuantity);
            }
        }
    }

    //@PostConstruct
    void generateUnitsRelations() {
        if (unitsRelationRepository.findAll().size() > 0) {
            LOGGER.debug("UnitRelations already generated");
        } else {
            itemService.addUnitsRelation(unitsRelation2);
            itemService.addUnitsRelation(unitsRelation);
            itemService.addUnitsRelation(unitsRelation3);
            itemService.addUnitsRelation(unitsRelation4);
        }

    }

}
