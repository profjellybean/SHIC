package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListCreationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShoppingListDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ShoppingListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class ShoppingListServiceImpl implements ShoppingListService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShoppingListRepository shoppingListRepository;

    public ShoppingListServiceImpl(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }


    public Long createNewShoppingList(ShoppingListCreationDto dto) {
        LOGGER.debug("Creating a new shoppinglist");

        return shoppingListRepository.saveAndFlush(   ShoppingList.ShoppingListBuilder.aShoppingList().withName(dto.getName()).build()  ).getId();
    }

    @Override
    public ShoppingList getShoppingListByid(Long id) {
        LOGGER.debug("Getting shoppinglist by id: {}",id);

        return shoppingListRepository.getById(id);
    }

}
