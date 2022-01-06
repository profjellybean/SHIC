package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IdStringCollectionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RegisterMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.access.annotation.Secured;

import javax.annotation.security.PermitAll;
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
    public RegisterDto findById(@PathVariable Long id) {
        LOGGER.info("GET /api/v1/register/{}", id);
        return registerMapper.registerToRegisterDto(registerService.findOne(id));
    }

    //@Secured("ROLE_USER")
    @PermitAll
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get detailed information about a specific register", security = @SecurityRequirement(name = "apiKey"))
    public RegisterDto confirmPayment(@Param("id") Long id, @Param("additionalId") Long additionalId,
                                      @Param("additionalString") String additionalString) {
        LOGGER.info("PUT /api/v1/register {}", id);
        return registerMapper.registerToRegisterDto(registerService.confirmPayment(id, additionalId, additionalString));
    }

    @Secured("ROLE_USER")
    //@PermitAll
    @GetMapping(value = "/monthlysum")
    @Operation(summary = "Get sum of all Bills in this month", security = @SecurityRequirement(name = "apiKey"))
    public Double billSumOfCurrentMonth(Authentication authentication) {
        LOGGER.info("Endpoint: GET /api/v1/register/monthlysum/{}", authentication);
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not logged-in");
        }
        return registerService.billSumOfCurrentMonth(authentication.getName());
    }

    @Secured("ROLE_USER")
    //@PermitAll
    @PutMapping(value = "/monthlybudget")
    @Operation(summary = "Edit Monthly Budget", security = @SecurityRequirement(name = "apiKey"))
    public Double billSumOfCurrentMonth(Authentication authentication, @Param("budget") Double budget) {
        LOGGER.info("Endpoint: Edit /api/v1/register/monthlybudget/{}{}", authentication, budget);
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not logged-in");
        }
        try {
            return registerService.editMonthlyBudget(authentication.getName(), budget);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
