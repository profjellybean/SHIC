package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.RegisterDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedMessageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RegisterMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.BillRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RegisterRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Arrays;
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

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private ItemStorageRepository itemStorageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    private Register register;

    private Register register2;

    private Register savedRegister;

    private Bill bill;

    private Bill savedBill;

    @BeforeEach
    public void beforeEach() {
        registerRepository.deleteAll();
        register = Register.RegisterBuilder.aRegister()
            .withBills(null)
            .withMonthlyPayment(300)
            .withMonthlyBudget(500)
            .build();
        savedRegister = registerRepository.saveAndFlush(register);
        bill = Bill.BillBuilder.aBill()
            .withId(1L)
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

    @Test
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
/*
    @Test
    public void return_RegisterDtoWhen_GivenValidUsername_RegisterId_AndBillId() throws Exception {
        String body = REGISTERENDPOINT_URI + "?id=" + savedRegister.getId() + "&additionalId=" + savedBill.getId() +
            "&additionalString=" + userRepository.getById(2L).getUsername();

        MvcResult mvcResult = this.mockMvc.perform(put(body)
                .contentType(MediaType.APPLICATION_JSON))
            //.header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(billRepository.getById(1L).getNotPaidNames().isEmpty());
    }


 */
    @Test
    public void throwNotFoundException_When_GivenRegisterWithUnknownId() throws Exception {
        Long unknownRegisterId = -100L;
        String body = REGISTERENDPOINT_URI + "?id=" + unknownRegisterId + "&additionalId=" + savedBill.getId() +
            "&additionalString=" + userRepository.getById(2L).getUsername();

        MvcResult mvcResult = this.mockMvc.perform(put(body)
                .contentType(MediaType.APPLICATION_JSON))
            //.header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andReturn();
    }

    @Test
    public void returnRegisterWhenGivenKnownId() throws Exception {

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

}
