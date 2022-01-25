package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BillDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.BillMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Bill;
import at.ac.tuwien.sepm.groupphase.backend.entity.Register;
import at.ac.tuwien.sepm.groupphase.backend.repository.BillRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RegisterRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class BillEndpointTest implements TestData {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtTokenizer jwtTokenizer;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RegisterRepository registerRepository;
    @Autowired
    private BillMapper billMapper;

    @Test
    public void insertInValidBillShouldNotWork() throws Exception {
        BillDto bill = new BillDto("TestBill");

        MvcResult mvcResult = this.mockMvc.perform(post(BILLENDPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bill))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();


        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void insertValidBillShouldWork() throws Exception {
        Register register = new Register();
        register = registerRepository.saveAndFlush(register);
        BillDto bill = new BillDto(null, null, "TestBill", null, null, 10000, 0, LocalDate.now(), register.getId());

        MvcResult mvcResult = this.mockMvc.perform(post(BILLENDPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bill))
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }


    //@Test
    public void gettingAllBillsShouldWork() throws Exception {
        Register register = new Register();
        register = registerRepository.saveAndFlush(register);
        BillDto bill = new BillDto(null, null, "TestBill", null, null, 10000, 0, LocalDate.now(), register.getId());
        billRepository.saveAndFlush(billMapper.billDtoToBill(bill));

        MvcResult mvcResult = this.mockMvc.perform(get(BILLENDPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(billRepository.findAll());
    }

    @Test
    public void gettingBillByIdShouldWork() throws Exception {
        Register register = new Register();
        register = registerRepository.saveAndFlush(register);
        BillDto bill = new BillDto(null, null, "TestBill", null, null, 10000, 0, LocalDate.now(), register.getId());
        Bill bill1 = billRepository.saveAndFlush(billMapper.billDtoToBill(bill));

        MvcResult mvcResult = this.mockMvc.perform(get(BILLENDPOINT_URI + "/" + bill1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(billRepository.findAll());
    }

    @Test
    public void deletingBillByIdShouldWork() throws Exception {
        Register register = new Register();
        register = registerRepository.saveAndFlush(register);
        BillDto bill = new BillDto(4L, null, "TestBill", null, null, 10000, 0, LocalDate.now(), register.getId());
        billRepository.saveAndFlush(billMapper.billDtoToBill(bill));

        MvcResult mvcResult = this.mockMvc.perform(delete(BILLENDPOINT_URI + "/" + 4)
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
}
