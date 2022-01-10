package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IdStringCollectionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TimeSumDto;
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
import org.springframework.format.annotation.DateTimeFormat;
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
import java.time.LocalDate;

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
            LOGGER.error("You are not logged-in");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not logged-in");
        }
        try {
            return registerService.billSumOfCurrentMonth(authentication.getName());
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    //@PermitAll
    @PutMapping(value = "/monthlybudget")
    @Operation(summary = "Edit Monthly Budget", security = @SecurityRequirement(name = "apiKey"))
    public Double billSumOfCurrentMonth(Authentication authentication, @Param("budget") Double budget) {
        LOGGER.info("Endpoint: Edit /api/v1/register/monthlybudget/{}{}", authentication, budget);
        if (authentication == null) {
            LOGGER.error("You are not logged-in");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not logged-in");
        }
        try {
            return registerService.editMonthlyBudget(authentication.getName(), budget);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (NotFoundException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/sumOfMonthAndYear")
    @Operation(summary = "Get sum of all Bills of specific Month and year", security = @SecurityRequirement(name = "apiKey"))
    public TimeSumDto billSumOfMonthAndYear(Authentication authentication, @Param("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        LOGGER.info("Endpoint: GET /api/v1/register/{}", authentication);
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not logged-in");
        }
        if (date == null) {
            return null;
        }
        return new TimeSumDto(registerService.billSumOfMonthAndYear(authentication.getName(), date), date);
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/sumOfYear")
    @Operation(summary = "Get sum of all Bills of specific year", security = @SecurityRequirement(name = "apiKey"))
    public TimeSumDto billSumOfYear(Authentication authentication, @Param("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        LOGGER.info("Endpoint: GET /api/v1/register/{}", authentication);
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not logged-in");
        }
        if (date == null) {
            return null;
        }
        return new TimeSumDto(registerService.billSumOfYear(authentication.getName(), date), date);
    }


}
