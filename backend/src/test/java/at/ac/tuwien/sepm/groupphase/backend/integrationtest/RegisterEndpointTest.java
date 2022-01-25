package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.RegisterDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.TestDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedMessageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RegisterMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
import at.ac.tuwien.sepm.groupphase.backend.entity.ShoppingList;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.BillRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RegisterRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.RegisterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class RegisterEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RegisterRepository registerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RegisterMapper registerMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    TestDataGenerator testDataGenerator;

    @Autowired
    private ItemStorageRepository itemStorageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    @Autowired
    private RegisterService registerService;

    @Autowired
    PlatformTransactionManager txm;

    TransactionStatus txstatus;

    private Register register;

    private Register register2;

    private Register savedRegister;

    private Bill bill;

    private Bill savedBill;

    @BeforeEach
    public void beforeEach() {
        registerRepository.deleteAll();
        register = Register.RegisterBuilder.aRegister()
            .withId(-10L)
            .withBills(null)
            .withMonthlyPayment(300)
            .withMonthlyBudget(500)
            .build();
        savedRegister = registerRepository.saveAndFlush(register);
        bill = Bill.BillBuilder.aBill()
            .withId(-10L)
            .withRegisterId(savedRegister.getId())
            .withGroceries(null)
            .withNotes("test")
            .withNames(null)
            .withNotPaidNames(null)
            .withSum(80)
            .withSumPerPerson(40)
            .withDate(null)
            .build();
        savedBill = billRepository.saveAndFlush(bill);

        savedBill.setNames(new HashSet<ApplicationUser>() {
            {
                add(userRepository.getById(2L));
            }
        });
        savedBill = billRepository.saveAndFlush(savedBill);
        savedRegister = registerRepository.saveAndFlush(savedRegister);

        savedBill.setNotPaidNames(new HashSet<ApplicationUser>() {
            {
                add(userRepository.getById(2L));
            }
        });
        savedBill = billRepository.saveAndFlush(savedBill);
        savedRegister = registerRepository.saveAndFlush(savedRegister);

        HashSet<Bill> billset = new HashSet<Bill>();
        billset.add(savedBill);
        savedRegister.setBills(billset);
        savedRegister = registerRepository.saveAndFlush(savedRegister);

        register2 = Register.RegisterBuilder.aRegister()
            .withBills(new HashSet<Bill>())
            .withMonthlyPayment(300)
            .withMonthlyBudget(500)
            .build();
        registerRepository.saveAndFlush(register2);
    }

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


    //@Test
    public void return_RegisterDtoWhen_GivenValidUsername_RegisterId_AndBillId() throws Exception {
        String body = REGISTERENDPOINT_URI + "?id=" + savedRegister.getId() + "&additionalId=" + savedBill.getId() +
            "&additionalString=" + userRepository.getById(2L).getUsername();

        MvcResult mvcResult = this.mockMvc.perform(put(body)
                .contentType(MediaType.APPLICATION_JSON)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(billRepository.getById(1L).getNotPaidNames().isEmpty());
    }

    @Test
    public void throwException_WhenGivenValidUsername_RegisterId_AndInvalidBillId() throws Exception {
        String body = REGISTERENDPOINT_URI + "?id=" + savedRegister.getId() + "&additionalId=-10" +
            "&additionalString=" + userRepository.getById(2L).getUsername();

        MvcResult mvcResult = this.mockMvc.perform(put(body)
                .contentType(MediaType.APPLICATION_JSON)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertThrows(NotFoundException.class, () -> registerService.confirmPayment(savedRegister.getId(),-10L, userRepository.getById(2L).getUsername()));
    }

    @Test
    public void throwException_WhenGivenValidUsername_BillId_AndInvalidRegisterId() throws Exception {
        String body = REGISTERENDPOINT_URI + "?id=-10" + "&additionalId=" + savedBill.getId() +
            "&additionalString=" + userRepository.getById(2L).getUsername();

        MvcResult mvcResult = this.mockMvc.perform(put(body)
                .contentType(MediaType.APPLICATION_JSON)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertThrows(NotFoundException.class, () -> registerService.confirmPayment(-10L,savedBill.getId(), userRepository.getById(2L).getUsername()));
    }

    @Test
    public void throwException_WhenGivenValidRegisterId_BillId_AndInvalidUsername() throws Exception {
        String body = REGISTERENDPOINT_URI + "?id=" + savedRegister.getId() + "&additionalId=" + savedBill.getId() +
            "&additionalString=timothy";

        MvcResult mvcResult = this.mockMvc.perform(put(body)
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertThrows(NotFoundException.class, () -> registerService.confirmPayment(savedRegister.getId(),savedBill.getId(), "timothy"));
    }

    @Test
    public void returnRegisterWhenGettingRegisterWithKnownId() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get(REGISTERENDPOINT_URI + "/{id}", register2.getId())
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertAll(
            () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
            () -> assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType())
        );

        RegisterDto registerDto = objectMapper.readValue(response.getContentAsString(),
            RegisterDto.class);

        assertEquals(register2, registerMapper.registerDtoToRegister(registerDto));
    }

    @Test
    public void throwExceptionWhenGettingRegisterWithUnknownId() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get(REGISTERENDPOINT_URI + "/{id}", -10L)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertThrows(NotFoundException.class, () -> registerService.findOne(-10L));
    }

    @Test
    public void givenBills_billSumOfCurrentMonth_then200() throws Exception {

        testDataGenerator.generateData_billSumOfCurrentMonth();

        MvcResult mvcResult = this.mockMvc.perform(get(REGISTERENDPOINT_URI + "/monthlysum")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("30.0", response.getContentAsString());

    }

    @Test
    public void givenNoBills_billSumOfCurrentMonth_thenSum0_then200() throws Exception {

        testDataGenerator.generateData_billSumOfCurrentMonth_noBills();

        MvcResult mvcResult = this.mockMvc.perform(get(REGISTERENDPOINT_URI + "/monthlysum")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("0.0", response.getContentAsString());

    }


    @Test
    public void calculateBillSumShouldWork() throws Exception {
        testDataGenerator.generateData_billSumOfCurrentMonth();

        MvcResult mvcResult = this.mockMvc.perform(get(REGISTERENDPOINT_URI + "/billSumGroup")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("30.0", response.getContentAsString());

    }

    @Test
    public void calculateBillSumUserShouldWork() throws Exception {
        testDataGenerator.generateData_billSum();

        MvcResult mvcResult = this.mockMvc.perform(get(REGISTERENDPOINT_URI + "/billSumUser")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("0.0", response.getContentAsString());

    }

    @Test
    public void calculateBillSumShouldWorkNoBills() throws Exception {
        testDataGenerator.generateData_billSumOfCurrentMonth_noBills();

        MvcResult mvcResult = this.mockMvc.perform(get(REGISTERENDPOINT_URI + "/billSumGroup")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("", response.getContentAsString());

    }

    @Test
    public void calculateBillSumUserShouldWorkNoBills() throws Exception {
        testDataGenerator.generateData_billSumOfCurrentMonth_noBills();

        MvcResult mvcResult = this.mockMvc.perform(get(REGISTERENDPOINT_URI + "/billSumUser")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(TEST_USER, USER_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("0.0", response.getContentAsString());

    }
}
