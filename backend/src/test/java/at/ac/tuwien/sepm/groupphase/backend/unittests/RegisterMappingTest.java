package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RegisterMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class RegisterMappingTest {

    private final Register register = new Register(null,null, 400, 450);
    private final RegisterDto registerDto = new RegisterDto(null, null, 400, 450);

    @Autowired
    RegisterMapper registerMapper;

    @Test
    public void givenNothing_whenMapRegisterEntityToDto_thenEntityHasAllProperties() {
        RegisterDto registerDto = registerMapper.registerToRegisterDto(register);
        assertAll(
            () -> assertEquals(register.getId(), registerDto.getId()),
            () -> assertEquals(register.getBills(), registerDto.getBills()),
            () -> assertEquals(register.getMonthlyPayments(), registerDto.getMonthlyPayments()),
            () -> assertEquals(register.getMonthlyBudget(), registerDto.getMonthlyBudget())
        );
    }

    @Test
    public void givenNothing_whenMapRegisterDtoToEntity_thenEntityHasAllProperties() {
        Register register = registerMapper.registerDtoToRegister(registerDto);

        assertAll(
            () -> assertEquals(registerDto.getId(), register.getId()),
            () -> assertEquals(registerDto.getBills(), register.getBills()),
            () -> assertEquals(registerDto.getMonthlyPayments(), register.getMonthlyPayments()),
            () -> assertEquals(registerDto.getMonthlyBudget(), register.getMonthlyBudget())
        );
    }
}
