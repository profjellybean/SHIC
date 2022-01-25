package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UnitOfQuantityDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.RecipeCategory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    String BILLENDPOINT_URI = BASE_URI + "/bill";
    String ADMIN_USER = "admin";
    String TEST_USER = "testUser";
    String STORAGEENDPOINT_URI = BASE_URI + "/storage";
    String SHOPPINGLISTENPOINDT_URI = BASE_URI + "/shoppinglist";
    String RECIPEENPOINT_URI = BASE_URI + "/recipe";
    String ITEMENDPOINT_UNITOFQUANTITY_URI = BASE_URI + "/item/unitOfQuantity";
    String ITEMENDPOINT_UNITRELATION_URI = BASE_URI + "/item/unitsRelation";
    String USERGROUPENDPOINT_URI = BASE_URI + "/group";
    String REGISTERENDPOINT_URI = BASE_URI + "/register";
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

    // Values for Recipe Tests
    String TEST_RECIPE_NAME = "RECIPE TEST NAME";
    String TEST_RECIPE_DESCRIPTION = "RECIPE TEST DESCRIPTION";
    Set<RecipeCategory> TEST_RECIPE_CATEGORIES = new HashSet<>() {
        {
            add(RecipeCategory.vegan);
        }
    };

    // Values for ShoppingList Tests
    String TEST_SHOPPINGLIST_NAME = "SHOPPINGLIST TEST NAME";
    String TEST_SHOPPINGLIST_NOTES = "SHOPPINGLIST TEST NOTES";


    // Values for ItemStorage Tests
    String TEST_ITEMSTORAGE_NAME = "ITEMSTORAGE TEST NAME";
    String TEST_ITEMSTORAGE_NOTES = "ITEMSTORAGE TEST NOTES";
    Long TEST_ITEMSTORAGE_ID = 1L;
    int TEST_ITEMSTORAGE_AMOUNT = 3;
    UnitOfQuantity TEST_ITEMSTORAGE_UNITOFQUANTITY = new UnitOfQuantity("kg");
    UnitOfQuantityDto TEST_ITEMSTORAGE_UNITOFQUANTITYDTO = new UnitOfQuantityDto("kg");
    Long TEST_ITEMSTORAGE_STORAGEID = 2L;
    Long TEST_ITEMSTORAGE_SHOPPINGLISTID = 3L;
}
