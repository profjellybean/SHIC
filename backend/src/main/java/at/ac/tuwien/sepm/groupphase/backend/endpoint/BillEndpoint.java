package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BillDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.BillMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.transaction.Transactional;
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

}
