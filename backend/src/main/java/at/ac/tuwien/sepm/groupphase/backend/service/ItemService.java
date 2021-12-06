package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.ItemServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface ItemService {
    UnitOfQuantity addUnitOfQuantity(UnitOfQuantity unitOfQuantity);
    List<UnitOfQuantity> getAll();

}
