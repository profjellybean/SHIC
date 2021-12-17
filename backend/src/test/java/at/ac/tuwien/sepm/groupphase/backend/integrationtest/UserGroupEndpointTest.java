package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserGroupRepository;
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

import java.net.URI;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.STORAGEENDPOINT_URI;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USERENDPOINT_URI;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USERGROUPENDPOINT_URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserGroupEndpointTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void generateNewGroup() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(USERGROUPENDPOINT_URI)).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        Long id = Long.valueOf(mvcResult.getResponse().getContentAsString());

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(userGroupRepository.findById(id).isPresent());
    }
}
