package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UnitOfQuantityDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitsRelation;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitsRelationRepository;
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

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ITEMENDPOINT_UNITOFQUANTITY_URI;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.STORAGEENDPOINT_URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ItemEndpointTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UnitsRelationRepository unitsRelationRepository;
    @Autowired
    private UnitOfQuantityRepository unitOfQuantityRepository;
    @Autowired
    private ItemStorageRepository itemStorageRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void insertUnityOfQuantityWithEmptyOrNullBaseUnitShouldThrowException() throws Exception {
        UnitOfQuantityDto unitOfQuantityDto = new UnitOfQuantityDto();

        MvcResult mvcResult = this.mockMvc.perform(post(ITEMENDPOINT_UNITOFQUANTITY_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unitOfQuantityDto)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void insertValidItem() throws Exception {
        UnitOfQuantityDto unitOfQuantityDto = new UnitOfQuantityDto("test");

        MvcResult mvcResult = this.mockMvc.perform(post(ITEMENDPOINT_UNITOFQUANTITY_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unitOfQuantityDto)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(unitOfQuantityRepository.findByName("test").isPresent());
    }
}
