package at.ac.tuwien.sepm.groupphase.backend.integrationtest;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ItemStorageDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Storage;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.StorageRepository;
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


import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.STORAGEENDPOINT_URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class StorageEndpointTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private ItemStorageRepository itemStorageRepository;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void insertItemWithEmptyOrNullStorageIdShouldThrowException() throws Exception {
        ItemStorageDto itemStorageDto = new ItemStorageDto();

        MvcResult mvcResult = this.mockMvc.perform(post(STORAGEENDPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemStorageDto)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void insertValidItem() throws Exception {
        ItemStorageDto itemStorageDto = new ItemStorageDto(-1L, "Test");
        storageRepository.saveAndFlush(new Storage(-1L));


        MvcResult mvcResult = this.mockMvc.perform(post(STORAGEENDPOINT_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemStorageDto)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(itemStorageRepository.findByName("Test").isPresent());
    }


    @Test
    public void insertItemsThenGetAll() throws Exception {
        ItemStorage itemStorageDto = new ItemStorage(-1L, "Test1");
        ItemStorage itemStorageDto1 = new ItemStorage(-1L, "Test2");
        ItemStorage itemStorageDto2 = new ItemStorage(-1L, "Test3");
        storageRepository.saveAndFlush(new Storage(-1L));
        itemStorageRepository.saveAndFlush(itemStorageDto);
        itemStorageRepository.saveAndFlush(itemStorageDto1);
        itemStorageRepository.saveAndFlush(itemStorageDto2);

        MvcResult mvcResult = this.mockMvc.perform(get(STORAGEENDPOINT_URI + "?id=", -1)
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(3, itemStorageRepository.findAll().size());
    }

    @Test
    public void insertItemStorageAndFindIt() throws Exception {
        storageRepository.saveAndFlush(new Storage(-1L));
        ItemStorage itemStorageDto = new ItemStorage(-1, "Test123");
        itemStorageRepository.saveAndFlush(itemStorageDto);

        MvcResult mvcResult = this.mockMvc.perform(get(STORAGEENDPOINT_URI + "?name=", "Test123")
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(1, itemStorageRepository.findAllByStorageIdAndNameContainingIgnoreCase(-1L, "Test123").size());
    }

    @Test
    public void tryToFindNonExistingItem() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(STORAGEENDPOINT_URI + "?name=", "Test1234567890")
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(0, itemStorageRepository.findAllByStorageIdAndNameContainingIgnoreCase(-1L, "Test123456786").size());
    }



    @Test
    public void searchForExistingItem() throws Exception {
        ItemStorageDto itemStorageDto = new ItemStorageDto(-1,"test2");
        storageRepository.saveAndFlush(new Storage(-1L));

        MvcResult mvcResult = this.mockMvc.perform(get(STORAGEENDPOINT_URI+"/search")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(itemStorageDto)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(),response.getStatus());

    }


}
