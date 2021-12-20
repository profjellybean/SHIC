package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UnitOfQuantityDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UnitsRelationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitsRelation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UnitsRelationMapper {
    UnitsRelationDto unitsRelationToUnitsRelationDto(UnitsRelation unitsRelation);

    UnitsRelation unitsRelationDtoToUnitsRelation(UnitsRelationDto unitsRelationDto);

    List<UnitsRelationDto> unitsRelationsToUnitsRelationsDto(List<UnitsRelation> unitsRelations);
}
