package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BillDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface BillMapper {

    BillDto billToBillDto(Bill bill);

    Bill billDtoToBill(BillDto billDto);

    List<BillDto> billListToBillDtoList(List<Bill> billList);
}
