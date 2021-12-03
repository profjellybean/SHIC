package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class UnitOfQuantityDataGenerator {

    private final UnitOfQuantityRepository unitOfQuantityRepository;

    public UnitOfQuantityDataGenerator(UnitOfQuantityRepository unitOfQuantityRepository){
        this.unitOfQuantityRepository=unitOfQuantityRepository;
    }
}
