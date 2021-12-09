package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;

//@Profile("generateData")
@Component
public class UnitOfQuantityDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UnitOfQuantityRepository unitOfQuantityRepository;
    private static final String[] UNITS = {"kg", "g", "L", "ml", "pieces", "can", "cup"};

    public UnitOfQuantityDataGenerator(UnitOfQuantityRepository unitOfQuantityRepository) {
        this.unitOfQuantityRepository = unitOfQuantityRepository;
    }

    @PostConstruct
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

}
