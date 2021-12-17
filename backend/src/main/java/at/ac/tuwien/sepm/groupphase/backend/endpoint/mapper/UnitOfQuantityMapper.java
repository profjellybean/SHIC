package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UnitOfQuantityDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UnitOfQuantityMapper {
    UnitOfQuantityDto unitOfQuantityToUnitOfQuantityDto(UnitOfQuantity unitOfQuantity);

    UnitOfQuantity unitOfQuantityDtoToUnitOfQuantity(UnitOfQuantityDto unitOfQuantityDto);

    List<UnitOfQuantityDto> unitsOfQuantityToUnitsOfQuantityDto(List<UnitOfQuantity> unitsOfQuantity);
}
