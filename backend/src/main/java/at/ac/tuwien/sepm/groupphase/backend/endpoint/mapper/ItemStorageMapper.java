package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ItemStorageMapper {

    ItemStorageDto itemStorageToItemStorageDto(ItemStorage itemStorage);

    List<ItemStorageDto> itemsStorageToItemsStorageDto(List<ItemStorage> itemStorages);

    ItemStorage itemStorageDtoToItemStorage(ItemStorageDto itemStorageDto);

}

