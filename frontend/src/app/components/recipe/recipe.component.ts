import {Component, OnInit, TemplateRef} from '@angular/core';
import {Recipe} from '../../dtos/recipe';
import {RecipeService} from '../../services/recipe.service';
import {Item} from '../../dtos/item';
import {ItemService} from '../../services/item.service';
import {UserService} from '../../services/user.service';
import {User} from '../../dtos/user';
// @ts-ignore
import jwt_decode from 'jwt-decode';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AuthService} from '../../services/auth.service';
import {NotificationsComponent} from '../notifications/notifications.component';


@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrls: ['./recipe.component.scss']
})
export class RecipeComponent implements OnInit {

  recipes: Recipe[] = null;
  ingredientsToAdd: Item[] = null;
  nullRecipe: Recipe = {
    name: null, id: null, categories: null, description: null, ingredients: this.ingredientsToAdd,
    groupId: null
  };

  popup = false;
  deleteRecipe: Recipe;
  recipeToAdd = this.nullRecipe;
  searchName = '';
  error: string;
  success: string;
  errorMessage = '';
  submitted = false;
  allItems: Item[];
  tempIngredient: Item;

  user: User = {
    // @ts-ignore
    username: jwt_decode(this.authService.getToken()).sub.trim(),
    id: null,
    currGroup: null,
    privList: null,
    email: null,
    image: null

  };

  constructor(
    private recipeService: RecipeService,
    private modalService: NgbModal,
    private itemService: ItemService,
    private userService: UserService,
    private authService: AuthService,
    private notifications: NotificationsComponent
  ) {
  }

  ngOnInit(): void {
    this.reloadRecipes();
    this.getAllItems();
    this.getCurrentGroup();
  }

  addTempIngredient() {
    console.log('Add ingredient to temp recipe', this.tempIngredient, this.tempIngredient.quantity.name);
    if (this.recipeToAdd.ingredients === null || this.recipeToAdd.ingredients === undefined) {
      this.recipeToAdd.ingredients = [];
    }
    this.tempIngredient.id = null;
    this.recipeToAdd.ingredients.push(this.tempIngredient);
    this.tempIngredient = undefined;
  }

  getAllItems() {
    this.itemService.findAll().subscribe({
      next: data => {
        console.log('received items', data);
        this.allItems = data;
      },
      error: error => {
        console.error(error.message);
        this.notifications.pushFailure('Error while getting all Items: ' + error.error.message);
      }
    });
  }


  openAddModal(recipeAddModal: TemplateRef<any>) {
    this.modalService.open(recipeAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  openDeleteModal(recipeDeleteModal: TemplateRef<any>) {
    this.modalService.open(recipeDeleteModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  addRecipeForm(form) {
    this.submitted = true;
    if (this.recipeToAdd.ingredients === undefined || this.recipeToAdd.ingredients === null) {
      this.error = 'Recipe needs ingredients';
    } else if (form.valid) {
      console.log('form item to add', this.recipeToAdd);
      this.addRecipe(this.recipeToAdd);
      this.clearForm();
      this.vanishError();
    }
  }

  delete(recipe: Recipe) {
    console.log('deleteRecipe', recipe.id);
    this.recipes = this.recipes.filter(r => r !== recipe);
    this.recipeService.deleteRecipeById(recipe.id).subscribe();
  }

  addRecipe(recipe: Recipe) {
    console.log('addRecipe', this.recipeToAdd);
    this.recipeToAdd.groupId = this.user.currGroup.id;
    this.recipeService.addRecipe(recipe).subscribe({
      next: data => {
        console.log('received recipes', data);
        this.reloadRecipes();
        this.notifications.pushSuccess('Recipe has been added successfully');
      },
      error: error => {
        console.error(error.message);
        this.notifications.pushFailure('Error while adding Recipe: ' + error.error.message);
      }
    });
  }

  reloadRecipes() {
    this.recipeService.findAll().subscribe({
      next: data => {
        console.log('received recipes', data);
        this.recipes = data;
      },
      error: error => {
        console.error(error.message);
        this.notifications.pushFailure('Error while loading Recipes: ' + error.error.message);
      }
    });
  }

  findAllByName(name: string) {
    this.recipeService.findAllByName(name).subscribe({
      next: data => {
        console.log('received recipes', data);
        this.recipes = data;
      },
      error: error => {
        console.error(error.message);
        this.notifications.pushFailure('Error while loading Recipes: ' + error.error.message);
      }
    });
  }

  getCurrentGroup() {
    this.userService.getCurrentUser({username: this.user.username}).subscribe({
      next: data => {
        this.user = data;
      },
      error: error => {
        console.error(error.message);
        this.notifications.pushFailure('Error while getting current Group: ' + error.error.message);
      }
    });
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = null;
  }

  public vanishSuccess(): void {
    this.success = null;
  }

  private clearForm() {
    this.tempIngredient = undefined;
    this.recipeToAdd = new Recipe();
    this.submitted = false;
  }
}
