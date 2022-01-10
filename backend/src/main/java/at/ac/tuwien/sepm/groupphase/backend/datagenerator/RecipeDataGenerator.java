package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ItemStorage;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.entity.UnitOfQuantity;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.RecipeCategory;
import at.ac.tuwien.sepm.groupphase.backend.repository.ItemStorageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UnitOfQuantityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//@Profile("generateData")
@Component
public class RecipeDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_RECIPES_TO_GENERATE = 5;
    private static final boolean CREATE_REAL_RECIPES = true;
    private static final String TEST_RECIPE_NAME = "Name";
    private static final String TEST_RECIPE_DESCRIPTION = "Description of Recipe";
    private static final Set<ItemStorage> TEST_RECIPE_INGREDIENTS = new HashSet<>(Arrays.asList(
        new ItemStorage("RecipeTestIngredient1", "notes", null, null, 10, null, null, null, null),
        new ItemStorage("RecipeTestIngredient2", "notes", null, null, 20, null, null, null, null),
        new ItemStorage("RecipeTestIngredient3", "notes", null, null, 30, null, null, null, null)));
    private static final Set<RecipeCategory> TEST_RECIPE_CATEGORIES = new HashSet<>(Arrays.asList(RecipeCategory.breakfast, RecipeCategory.vegetarian));

    private final RecipeRepository recipeRepository;
    private final ItemStorageRepository itemStorageRepository;

    private final UnitOfQuantityDataGenerator unitOfQuantityDataGenerator;
    private final UnitOfQuantityRepository unitOfQuantityRepository;

    public RecipeDataGenerator(RecipeRepository recipeRepository, ItemStorageRepository itemStorageRepository,
                               UnitOfQuantityDataGenerator unitOfQuantityDataGenerator, UnitOfQuantityRepository unitOfQuantityRepository) {
        this.recipeRepository = recipeRepository;
        this.itemStorageRepository = itemStorageRepository;
        this.unitOfQuantityDataGenerator = unitOfQuantityDataGenerator;
        this.unitOfQuantityRepository = unitOfQuantityRepository;
    }

    //@PostConstruct
    public void generateRecipes() {
        if (recipeRepository.findAll().size() > 0) {
            LOGGER.debug("recipes already generated");
        } else if (CREATE_REAL_RECIPES) {
            LOGGER.debug("generating real recipes");

            this.unitOfQuantityDataGenerator.generateUnitOfQuantity();

            List<UnitOfQuantity> unitList = unitOfQuantityRepository.findAll();
            Map<String, UnitOfQuantity> mappedUnits = new HashMap<>();
            for (UnitOfQuantity unit :
                unitList) {
                mappedUnits.put(unit.getName(), unit);
            }
            // FETA NOODLES
            // generate recipe
            Recipe fetaCheeseRecipe = Recipe.RecipeBuilder.aRecipe()
                .withName("Feta Cheese Noodles")
                .withDescription("Noodles with feta cheese: "
                    + "\n Put the cheese in the oven at 180 degrees Celsius for approximately 15 minutes. "
                    + "Cook the noodles in that time and mix the cheese into the pot once its finished. "
                    + "Add some water to make it more creamy")
                .withCategories(new HashSet<>(Arrays.asList(RecipeCategory.vegetarian, RecipeCategory.dinner)))
                .build();
            LOGGER.debug("saving recipe {}", fetaCheeseRecipe);

            // generate ingredients
            //ItemStorage noodles = new ItemStorage("Noodles", "preferably small noodle and not spaghetti", null, null, 300, null, UnitOfQuantity.g, null);
            ItemStorage noodles = new ItemStorage("Noodles", "preferably small noodle and not spaghetti", null, null, 150, null, mappedUnits.get("g"), null, null);
            LOGGER.debug("saving ingredient {}", noodles);
            noodles = itemStorageRepository.save(noodles);
            //ItemStorage feta = new ItemStorage("Feta", "any kind will do", null, null, 2, null, UnitOfQuantity.pieces, null);
            ItemStorage feta = new ItemStorage("Feta", "any kind will do", null, null, 1, null, mappedUnits.get("pieces"), null, null);
            LOGGER.debug("saving ingredient {}", feta);
            feta = itemStorageRepository.save(feta);
            Recipe storedFetaCheeseRecipe = recipeRepository.save(fetaCheeseRecipe);
            storedFetaCheeseRecipe.setIngredients(new HashSet<>(Arrays.asList(noodles, feta)));
            recipeRepository.save(storedFetaCheeseRecipe);

            // potato wedges
            // generate recipe
            Recipe potatoWedgesRecipe = Recipe.RecipeBuilder.aRecipe()
                .withName("Potato Wedges")
                .withDescription("Cut the potatoes into small pieces and put them into the oven "
                    + "at 200 degrees Celsius until they are gold-brown. Add salt and spices at will.")
                .withCategories(new HashSet<>(Arrays.asList(RecipeCategory.vegetarian, RecipeCategory.dinner)))
                .build();
            LOGGER.debug("saving recipe {}", potatoWedgesRecipe);
            Recipe storedPotatoWedgesRecipe = recipeRepository.save(potatoWedgesRecipe);
            // generate ingredients
            //ItemStorage potato = new ItemStorage("Potatoes", "any kind", null, null, 400, null, UnitOfQuantity.g, null);
            ItemStorage potato = new ItemStorage("Potatoes", "any kind", null, null, 200, null, mappedUnits.get("g"), null, null);
            LOGGER.debug("saving ingredient {}", potato);
            potato = itemStorageRepository.save(potato);
            storedPotatoWedgesRecipe.setIngredients(new HashSet<>(Arrays.asList(potato)));
            recipeRepository.save(storedPotatoWedgesRecipe);

            // noodles with pesto
            // generate recipe
            Recipe noodlesWithPesto = Recipe.RecipeBuilder.aRecipe()
                .withName("Noodles with Pesto")
                .withDescription("Cook noodles and add Pesto")
                .withCategories(new HashSet<>(Arrays.asList(RecipeCategory.vegetarian, RecipeCategory.dinner)))
                .build();
            LOGGER.debug("saving recipe {}", noodlesWithPesto);

            // generate ingredients
            //ItemStorage pesto = new ItemStorage("Pesto", "any kind", null, null, 100, null, UnitOfQuantity.g, null);
            ItemStorage pesto = new ItemStorage("Pesto", "any kind", null, null, 150, null, mappedUnits.get("g"), null, null);
            LOGGER.debug("saving ingredient {}", pesto);
            pesto = itemStorageRepository.save(pesto);
            //ItemStorage noodlesForPesto = new ItemStorage("Noodles", "any kind", null, null, 100, null, UnitOfQuantity.g, null);
            ItemStorage noodlesForPesto = new ItemStorage("Noodles", "any kind", null, null, 50, null, mappedUnits.get("g"), null, null);
            LOGGER.debug("saving ingredient {}", noodlesForPesto);
            noodlesForPesto = itemStorageRepository.save(noodlesForPesto);
            Recipe storedPestoNoodles = recipeRepository.save(noodlesWithPesto);
            storedPestoNoodles.setIngredients(new HashSet<>(Arrays.asList(pesto, noodlesForPesto)));
            recipeRepository.save(storedPestoNoodles);

            // nutella cake
            // generate recipe
            Recipe nutellacake = Recipe.RecipeBuilder.aRecipe()
                .withName("Nutella Cake")
                .withDescription("First stir the eggs, than add hot nutella and mix both together. Last bake it at 175 Celcius")
                .withCategories(new HashSet<>(Arrays.asList(RecipeCategory.vegetarian, RecipeCategory.dinner)))
                .build();
            LOGGER.debug("saving recipe {}", nutellacake);

            // generate ingredients
            //ItemStorage pesto = new ItemStorage("Pesto", "any kind", null, null, 100, null, UnitOfQuantity.g, null);
            ItemStorage nutella = new ItemStorage("Nutella", "any kind", null, null, 60, null, mappedUnits.get("g"), null, null);
            LOGGER.debug("saving ingredient {}", nutella);
            nutella = itemStorageRepository.save(nutella);
            //ItemStorage noodlesForPesto = new ItemStorage("Noodles", "any kind", null, null, 100, null, UnitOfQuantity.g, null);
            ItemStorage eggs = new ItemStorage("Eggs", "any kind", null, null, 1, null, mappedUnits.get("pieces"), null, null);
            LOGGER.debug("saving ingredient {}", eggs);
            eggs = itemStorageRepository.save(eggs);
            Recipe storedNutellaCake = recipeRepository.save(nutellacake);
            storedNutellaCake.setIngredients(new HashSet<>(Arrays.asList(nutella, eggs)));
            recipeRepository.save(storedNutellaCake);

            // Pudding
            // generate recipe
            Recipe veganPudding = Recipe.RecipeBuilder.aRecipe()
                .withName("vegan Pudding")
                .withDescription("Mix both together and heat it in the microwave.")
                .withCategories(new HashSet<>(Arrays.asList(RecipeCategory.vegetarian, RecipeCategory.dinner)))
                .build();
            LOGGER.debug("saving recipe {}", veganPudding);

            // generate ingredients
            //ItemStorage pesto = new ItemStorage("Pesto", "any kind", null, null, 100, null, UnitOfQuantity.g, null);
            ItemStorage soyaMilk = new ItemStorage("Soya Milk", "any kind", null, null, 200, null, mappedUnits.get("ml"), null, null);
            LOGGER.debug("saving ingredient {}", soyaMilk);
            soyaMilk = itemStorageRepository.save(soyaMilk);
            //ItemStorage noodlesForPesto = new ItemStorage("Noodles", "any kind", null, null, 100, null, UnitOfQuantity.g, null);
            ItemStorage puddingMix = new ItemStorage("Pudding Mix", "any kind", null, null, 1, null, mappedUnits.get("pieces"), null, null);
            LOGGER.debug("saving ingredient {}", puddingMix);
            puddingMix = itemStorageRepository.save(puddingMix);
            Recipe storedVeganPudding = recipeRepository.save(veganPudding);
            storedVeganPudding.setIngredients(new HashSet<>(Arrays.asList(soyaMilk, puddingMix)));
            recipeRepository.save(storedVeganPudding);

        } else {
            LOGGER.debug("generating {} recipes", NUMBER_OF_RECIPES_TO_GENERATE);
            for (int i = 0; i < NUMBER_OF_RECIPES_TO_GENERATE; i++) {
                Recipe recipe = Recipe.RecipeBuilder.aRecipe()
                    .withName(TEST_RECIPE_NAME + " " + i)
                    .withDescription(TEST_RECIPE_DESCRIPTION + " " + i)
                    //.withIngredients(new HashSet<>(Arrays.asList(new ItemStorage(1L), new ItemStorage(2L))))
                    .withCategories(TEST_RECIPE_CATEGORIES)
                    .build();
                LOGGER.debug("saving recipe {}", recipe);
                Recipe storedRecipe = recipeRepository.save(recipe);
                //ItemStorage itemStorage = new ItemStorage("recipe ingredient in recipe "+i, "notes for itemStorage "+i, null, null, 10, Location.fridge, UnitOfQuantity.kg, 1L);
                ItemStorage itemStorage = new ItemStorage("recipe ingredient in recipe " + i, "notes for itemStorage " + i, null, null, 10, Location.fridge.toString(), null, null, null);
                itemStorage = itemStorageRepository.save(itemStorage);
                storedRecipe.setIngredients(new HashSet<>(Arrays.asList(itemStorage)));
                recipeRepository.save(storedRecipe);
            }
        }
    }
}
