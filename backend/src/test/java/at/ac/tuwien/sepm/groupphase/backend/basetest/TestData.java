package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.datagenerator.MasterDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.ShoppingListDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.StorageDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShoppingListRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface TestData {

    Long ID = 1L;
    String TEST_NEWS_TITLE = "Title";
    String TEST_NEWS_SUMMARY = "Summary";
    String TEST_NEWS_TEXT = "TestMessageText";
    LocalDateTime TEST_NEWS_PUBLISHED_AT =
        LocalDateTime.of(2019, 11, 13, 12, 15, 0, 0);

    String BASE_URI = "/api/v1";
    String MESSAGE_BASE_URI = BASE_URI + "/messages";
    String USERENDPOINT_URI = BASE_URI + "/user";
    String ADMIN_USER = "admin";
    String STORAGEENDPOINT_URI = BASE_URI + "/storage";
    String ITEMENDPOINT_UNITOFQUANTITY_URI = BASE_URI + "/item/unitOfQuantity";
    String ITEMENDPOINT_UNITRELATION_URI = BASE_URI + "/item/unitsRelation";
    String USERGROUPENDPOINT_URI = BASE_URI + "/group";
    String SHOPPINGLIST_ENDPOINT_URI = BASE_URI + "/shoppinglist";
    //String ADMIN_USER = "admin@email.com";
    List<String> ADMIN_ROLES = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_USER");
        }
    };
    String DEFAULT_USER = "admin";
    List<String> USER_ROLES = new ArrayList<>() {
        {
            add("ROLE_USER");
        }
    };

}
