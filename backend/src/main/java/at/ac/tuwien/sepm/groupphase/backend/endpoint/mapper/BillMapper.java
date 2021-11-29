package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BillDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import org.mapstruct.Mapper;

@Mapper
public interface BillMapper {

    Bill billToBillDto(Bill bill);

    BillDto billDtoToBill(BillDto billDto);
}
