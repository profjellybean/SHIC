package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.LocationClass;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface LocationMapper {

    LocationDto locationToLocationDto(LocationClass locationClass);

    List<LocationDto> locationToLocationDto(List<LocationClass> locationClasses);

    LocationClass locationDtoToLocation(LocationDto locationDto);

    List<LocationClass> locationDtoToLocation(List<LocationDto> locationDtos);

}
