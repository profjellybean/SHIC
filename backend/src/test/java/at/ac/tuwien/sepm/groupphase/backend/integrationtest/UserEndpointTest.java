package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.repository.CustomUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomUserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final int GENERATE_USER_COUNT = 50; //Ein zu hoher Wert erhöht die Dauer des Testlaufes dramatisch


    @Test
    void registerUser() throws Exception {
        UserLoginDto testUser = new UserLoginDto("TestUser1", "passwort1245");

        MvcResult mvcResult = this.mockMvc.perform(post(USERENDPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();


        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertTrue(userRepository.findUserByUsername(testUser.getUsername()).isPresent());

    }

    @Test
    void registerUnprocessableUser() throws Exception {
        UserLoginDto unprocessableUser = new UserLoginDto("UnprocessableUser1", "shortpw");

        MvcResult mvcResult = this.mockMvc.perform(post(USERENDPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unprocessableUser)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());


    }

    @Test
    void registerDuplicateUser() throws Exception {
        UserLoginDto testUser1 = new UserLoginDto("Polo_G", "correctPassword");
        UserLoginDto testUser2 = new UserLoginDto("Polo_G", "letMeIn2000");

        MvcResult mvcResult1 = this.mockMvc.perform(post(USERENDPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser1)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response1 = mvcResult1.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response1.getStatus());
        assertTrue(userRepository.findUserByUsername(testUser1.getUsername()).isPresent());


        MvcResult mvcResult2 = this.mockMvc.perform(post(USERENDPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser2)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response2 = mvcResult2.getResponse();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response2.getStatus());

    }


    @Test
    void registerManyUsers() throws Exception {
        List<UserLoginDto> userList = new LinkedList<>();
        generateUsers(userList, GENERATE_USER_COUNT);
        registerAllValidUsers(userList);
        checkExistenceOfUsers(userList);
        assertTrue(userRepository.findUserByUsername("SomeUserNeverAdded").isEmpty());

    }

    void checkExistenceOfUsers(List<UserLoginDto> users) {
        for (UserLoginDto user : users) {
            assertTrue(userRepository.findUserByUsername(user.getUsername()).isPresent());
        }

    }

    void registerAllValidUsers(List<UserLoginDto> users) throws Exception {

        for (UserLoginDto user : users) {
            MvcResult mvcResult1 = this.mockMvc.perform(post(USERENDPOINT_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andReturn();
            MockHttpServletResponse response1 = mvcResult1.getResponse();
            assertEquals(HttpStatus.CREATED.value(), response1.getStatus());
        }

    }

    void generateUsers(List<UserLoginDto> list, int count) {
        Random random = new Random();
        String[] gods = {
            "Jupiter",
            "Neptun",
            "Iuno",
            "Ceres",
            "Apollo",
            "Diana",
            "Minerva",
            "Mars",
            "Venus",
            "Mercurius",
            "Vulcanus",
            "Baccus",
            "Vesta",
            "Pluto",
            "Proserpina",
            "Herkules",
            "Iuventas"
        };
        int x;
        for (int c = 0; c < count; c++) {
            x = random.nextInt(gods.length);
            list.add(new UserLoginDto(gods[x] + c, "password" + c));
        }


    }



}
