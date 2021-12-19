package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.mapstruct.Mapper;


public interface UserMapper {
    ApplicationUser userDtoToUser(UserDto userDto);

    UserDto userToUserDto(ApplicationUser user);

    ApplicationUser dtoToEntity(UserRegistrationDto userRegistrationDto, Long shoppingListId, Long confirmationToken);
}
