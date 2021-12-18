package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.RegisterDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RegisterDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RegisterMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        /*
        RegisterDataGenerator dataGenerator = new RegisterDataGenerator(itemStorageRepository, userRepository,
            billRepository, registerRepository, shoppingListRepository);
        dataGenerator.generateRegister();

         */
    }

    @Test
    public void return_RegisterDtoWhen_GivenValidUsername_RegisterId_AndBillId() throws Exception {
        String body = REGISTERENDPOINT_URI + "?id=" + savedRegister.getId() + "&additionalId=" + savedBill.getId() +
            "&additionalString=" + userRepository.getById(2L).getUsername();
        System.out.println(body);
        MvcResult mvcResult = this.mockMvc.perform(put(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn();
            //.header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        //assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        assertTrue(billRepository.getById(1L).getNotPaidNames().isEmpty());
/*
        RegisterDto registerDto2 = objectMapper.readValue(response.getContentAsString(),
            RegisterDto.class);
        System.out.println(registerDto2.toString());

 */
    }

}
