package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UnitOfQuantityRepository unitOfQuantityRepository;

    @Autowired
    public ItemServiceImpl(UnitOfQuantityRepository unitOfQuantityRepository) {
        this.unitOfQuantityRepository = unitOfQuantityRepository;
    }

    @Override
    public UnitOfQuantity addUnitOfQuantity(UnitOfQuantity unitOfQuantity) {
        LOGGER.debug("Save unitOfQuanity");
        System.out.println(unitOfQuantity.toString());
        unitOfQuantityRepository.save(unitOfQuantity);
        return unitOfQuantity;
    }

    @Override
    public List<UnitOfQuantity> getAll() {
        LOGGER.debug("Getting all units of quantity");
        return unitOfQuantityRepository.findAll();
    }


}
