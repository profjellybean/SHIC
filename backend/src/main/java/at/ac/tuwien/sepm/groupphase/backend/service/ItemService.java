package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;

import java.util.List;

public interface ItemService {
    UnitOfQuantity addUnitOfQuantity(UnitOfQuantity unitOfQuantity);

    List<UnitOfQuantity> getAll();
}
