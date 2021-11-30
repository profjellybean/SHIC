package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RegisterConfirmPaymentDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RegisterMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
import at.ac.tuwien.sepm.groupphase.backend.service.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping(value = "/api/v1/register")
public class RegisterEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RegisterService registerService;
    private final RegisterMapper registerMapper;

    @Autowired
    public RegisterEndpoint(RegisterService registerService, RegisterMapper registerMapper) {
        this.registerService = registerService;
        this.registerMapper = registerMapper;
    }

    //@Secured("ROLE_USER")
    @PermitAll
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get detailed information about a specific register", security = @SecurityRequirement(name = "apiKey"))
    public RegisterDto find(@PathVariable Long id) {
        LOGGER.info("GET /api/v1/register/{}", id);
        return registerMapper.registerToRegisterDto(registerService.findOne(id));
    }

    //@Secured("ROLE_USER")
    @PermitAll
    @PutMapping
    @Operation(summary = "Get detailed information about a specific register", security = @SecurityRequirement(name = "apiKey"))
    public RegisterDto confirmPayment(@Valid @RequestBody RegisterConfirmPaymentDto registerConfirmPaymentDto) {
        LOGGER.info("PUT /api/v1/register/{}", registerConfirmPaymentDto);
        Register register = registerMapper.registerConfirmPaymentDtoToRegister(registerConfirmPaymentDto);
        return registerMapper.registerToRegisterDto(registerService.confirmPayment(register.getId(),
            registerConfirmPaymentDto.getBill().getId(), registerConfirmPaymentDto.getUser().getId()));
    }


}
