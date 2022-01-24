package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrashOrUsedDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.TrashOrUsed;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UsedOrTrashMapper {

    TrashOrUsedDto trashOrUsedToTrashOrUsedDto(TrashOrUsed trashOrUsed);

    TrashOrUsed trashOrUsedDtoToTrashOrUsed(TrashOrUsedDto trashOrUsedDto);

    List<TrashOrUsedDto> trashOrUsedsToTrashOrUsedDtos(List<TrashOrUsed> trashOrUseds);
}