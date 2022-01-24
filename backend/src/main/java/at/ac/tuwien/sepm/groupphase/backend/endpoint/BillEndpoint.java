package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BillDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.BillMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;


import javax.annotation.security.PermitAll;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/bill")
public class BillEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final BillService billService;
    private final BillMapper billMapper;

    @Autowired
    public BillEndpoint(BillService billService, BillMapper billMapper) {
        this.billService = billService;
        this.billMapper = billMapper;
    }

    //@Secured("ROLE_USER")
    @Transactional
    @PermitAll
    @GetMapping
    @Operation(summary = "Get list of bills", security = @SecurityRequirement(name = "apiKey"))
    public List<BillDto> findAll() {
        LOGGER.info("GET /api/v1/bill");
        return billMapper.billListToBillDtoList(billService.findAll());
    }

    //@Secured("ROLE_USER")
    @PermitAll
    @GetMapping(value = "/{id}")
    @Operation(summary = "Publish a new message", security = @SecurityRequirement(name = "apiKey"))
    public BillDto findById(@PathVariable Long id) {
        LOGGER.info("POST /api/v1/bill {}", id);
        return billMapper.billToBillDto(billService.findOne(id));
    }



    @PostMapping
    @PermitAll
    @Transactional
    @Operation(summary = "create a new bill")
    public BillDto bill(@RequestBody @Valid BillDto billDto) {                          //TODO Dicke Sicherheitslücke
        LOGGER.info("POST /recipe new bill {}", billDto);
        return billMapper.billToBillDto(billService.bill(billMapper.billDtoToBill(billDto)));
    }

    @PutMapping
    @PermitAll
    @Operation(summary = "Update a existing bill")
    public BillDto updateBill(Authentication authentication, @Valid @RequestBody BillDto billDto) {
        LOGGER.info("PUT /bill body: {}", billDto);
        try {
            return billMapper.billToBillDto(billService.updateBill(billMapper.billDtoToBill(billDto)));
        } catch (ServiceException s) {
            LOGGER.error(s.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        } catch (ValidationException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PermitAll
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBillById(Authentication authentication, @PathVariable Long id) {
        LOGGER.info("Endpoint: DELETE /bill/" + id);
        try {
            this.billService.deleteById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


}
