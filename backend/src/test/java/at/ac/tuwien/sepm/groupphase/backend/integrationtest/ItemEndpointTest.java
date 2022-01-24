package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UnitOfQuantityDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UnitsRelationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UnitOfQuantityMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitsRelation;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitsRelationRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ITEMENDPOINT_UNITOFQUANTITY_URI;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ITEMENDPOINT_UNITRELATION_URI;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.STORAGEENDPOINT_URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ItemEndpointTest implements TestData {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenizer jwtTokenizer;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private UnitsRelationRepository unitsRelationRepository;
    @Autowired
    private UnitOfQuantityRepository unitOfQuantityRepository;
    @Autowired
    private ItemStorageRepository itemStorageRepository;
    @Autowired
    private UnitOfQuantityMapper unitOfQuantityMapper;
    @Autowired
    private ObjectMapper objectMapper;

/*
    @Test
    @Disabled
    public void insertUnitOfQuantityWithEmptyOrNullNameShouldThrowException() throws Exception {
        UnitOfQuantityDto unitOfQuantityDto = new UnitOfQuantityDto();

        MvcResult mvcResult = this.mockMvc.perform(post(ITEMENDPOINT_UNITOFQUANTITY_URI + "?name=")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unitOfQuantityDto)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();




    // @BeforeEach
    // public void beforeEach() {
    //     unitsRelationRepository.deleteAll();
    //     unitOfQuantityRepository.deleteAll();
    // }

    @Autowired
    PlatformTransactionManager txm;

    TransactionStatus txstatus;

    @BeforeEach
    public void setupDBTransaction() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        txstatus = txm.getTransaction(def);
        assumeTrue(txstatus.isNewTransaction());
        txstatus.setRollbackOnly();
    }

    @AfterEach
    public void tearDownDBData() {
        txm.rollback(txstatus);
    }

    @Test
    public void insertUnitOfQuantityWithEmptyOrNullNameShouldThrowException() throws Exception {
        UnitOfQuantityDto unitOfQuantityDto = new UnitOfQuantityDto();

        MvcResult mvcResult = this.mockMvc.perform(post(ITEMENDPOINT_UNITOFQUANTITY_URI+"?name=")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unitOfQuantityDto)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void insertValidUnitOfQuantity() throws Exception {
        UnitOfQuantityDto unitOfQuantityDto = new UnitOfQuantityDto("test");

        MvcResult mvcResult = this.mockMvc.perform(post(ITEMENDPOINT_UNITOFQUANTITY_URI+"?name=test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unitOfQuantityDto)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(unitOfQuantityRepository.findByName("test").isPresent());
    }




    @Test
    public void insertValidUnitRelation() throws Exception {
        unitsRelationRepository.deleteAll();
        UnitOfQuantityDto unitOfQuantityDto = new UnitOfQuantityDto("UoQ1");
        UnitOfQuantityDto unitOfQuantityDto2 = new UnitOfQuantityDto("UoQ2");
        UnitsRelationDto unitsRelationDto = new UnitsRelationDto("UoQ1", "UoQ2", 100.0);

        unitOfQuantityRepository.save(unitOfQuantityMapper.unitOfQuantityDtoToUnitOfQuantity(unitOfQuantityDto2));
        unitOfQuantityRepository.save(unitOfQuantityMapper.unitOfQuantityDtoToUnitOfQuantity(unitOfQuantityDto));
        MvcResult mvcResult = this.mockMvc.perform(post(ITEMENDPOINT_UNITRELATION_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unitsRelationDto)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(unitsRelationRepository.findUnitsRelationByBaseUnitAndCalculatedUnit(unitOfQuantityDto.getName(), unitOfQuantityDto2.getName()));
    }




    @Test
    public void insertInvalidUnitRelation() throws Exception {
        UnitsRelationDto unitsRelationDto = new UnitsRelationDto();

        MvcResult mvcResult = this.mockMvc.perform(post(ITEMENDPOINT_UNITRELATION_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unitsRelationDto)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    /*
    @Test
    public void insertUnitsOfQuantitiesThenGetAll() throws Exception {
        unitOfQuantityRepository.deleteAll();

        UnitOfQuantity unitOfQuantity1 = new UnitOfQuantity("test1");
        UnitOfQuantity unitOfQuantity2 = new UnitOfQuantity("test2");
        UnitOfQuantity unitOfQuantity3 = new UnitOfQuantity("test3");

        unitOfQuantityRepository.save(unitOfQuantity1);
        unitOfQuantityRepository.save(unitOfQuantity2);
        unitOfQuantityRepository.save(unitOfQuantity3);

        MvcResult mvcResult = this.mockMvc.perform(get(ITEMENDPOINT_UNITOFQUANTITY_URI)
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();


        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(3,unitOfQuantityRepository.findAll().size());
    }


     */

    @Test
    public void insertUnitsRelationsThenGetAll() throws Exception {
        unitsRelationRepository.deleteAll();

        UnitOfQuantity unitOfQuantity1 = new UnitOfQuantity("test1");
        UnitOfQuantity unitOfQuantity2 = new UnitOfQuantity("test2");
        UnitOfQuantity unitOfQuantity3 = new UnitOfQuantity("test3");
        UnitOfQuantity unitOfQuantity4 = new UnitOfQuantity("test4");

        unitOfQuantityRepository.save(unitOfQuantity1);
        unitOfQuantityRepository.save(unitOfQuantity2);
        unitOfQuantityRepository.save(unitOfQuantity3);
        unitOfQuantityRepository.save(unitOfQuantity4);

        unitsRelationRepository.save(new UnitsRelation("test1", "test2", 100.0));
        unitsRelationRepository.save(new UnitsRelation("test4", "test3", 0.1));

        MvcResult mvcResult = this.mockMvc.perform(get(ITEMENDPOINT_UNITRELATION_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(2, unitsRelationRepository.findAll().size());
    }



    @Test
    public void GetNonExistingUnitOfQuantity() throws Exception {
        //unitOfQuantityRepository.deleteAll(); TODO

        MvcResult mvcResult = this.mockMvc.perform(get(ITEMENDPOINT_UNITOFQUANTITY_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNull(unitOfQuantityRepository.getUnitOfQuantityById(100000L));
    }




}
