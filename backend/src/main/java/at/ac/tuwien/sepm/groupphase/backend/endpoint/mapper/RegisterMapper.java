package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RegisterConfirmPaymentDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
import org.mapstruct.Mapper;

@Mapper
public interface RegisterMapper {

    Register registerDtoToRegister(RegisterDto registerDto);

    RegisterDto registerToRegisterDto(Register register);

    Register registerConfirmPaymentDtoToRegister(RegisterConfirmPaymentDto registerConfirmPaymentDto);



}
