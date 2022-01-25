import {Component, OnInit, TemplateRef} from '@angular/core';
import {Recipe} from '../../dtos/recipe';
import {Item} from '../../dtos/item';
import {RecipeService} from '../../services/recipe.service';
import {ActivatedRoute} from '@angular/router';
import {ShoppingListService} from '../../services/shopping-list.service';
import {ShowItem} from '../../dtos/ShowItem';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ItemService} from '../../services/item.service';
import {StorageService} from '../../services/storage.service';
import {NotificationsComponent} from '../notifications/notifications.component';
import {User} from '../../dtos/user';
import jwt_decode from 'jwt-decode';
import {UserService} from '../../services/user.service';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-recipe-detail',
  templateUrl: './recipe-detail.component.html',
  styleUrls: ['./recipe-detail.component.scss']
})
export class RecipeDetailComponent implements OnInit {

  recipe: Recipe = {
    id: null, name: null, description: null, ingredients: [], categories: [], groupId: null
  };
  recipeToUpdate: Recipe = {
    id: null, name: null, description: null, ingredients: [], categories: [], groupId: null
  };
  name: string;
  quantities: number[];
  unitOfQuantity: string;
  ingredients: Item[];
  items;
  deletedItems;
  ingredientsShow: ShowItem[];
  showItem: ShowItem;
  submitted = false;
  tempIngredient: Item;
  allItems: Item[];
  numberOfPeople = 0;
  user: User = {
    // @ts-ignore
    username: jwt_decode(this.authService.getToken()).sub.trim(),
    id: null,
    currGroup: null,
    privList: null,
    email: null,
    image: null

  };


  error: string;
  success: string;
  errorMessage = '';
  private expDate: number;

  constructor(private recipeService: RecipeService,
              private route: ActivatedRoute,
              private shoppingListService: ShoppingListService,
              private modalService: NgbModal,
              private itemService: ItemService,
              private storageService: StorageService,
              private notifications: NotificationsComponent,
              private userService: UserService,
              private authService: AuthService) {
  }

  ngOnInit(): void {
    this.getCurrentGroup();
    this.recipe.id = this.route.snapshot.params.id;
    this.findRecipeById(this.recipe.id);
    this.getAllItems();
  }

  getCurrentGroup() {
    this.userService.getCurrentUser({username: this.user.username}).subscribe({
      next: data => {
        this.user = data;
        data.currGroup.user.forEach(x => this.numberOfPeople++);
      },
      error: error => {
        console.error(error.message);
        this.notifications.pushFailure('Error while getting current Group: ' + error.error.message);
      }
    });
  }

  /*
    changeItemsToShowItems(){
      this.recipe.ingredients.forEach(element => this.ingredientsShow.push(this.changeItemToShowItem(element)));
    }

    changeItemToShowItem(item: Item): ShowItem{
      this.showItem.name= item.name;
      this.showItem.id=item.id;
      this.showItem.amount=item.amount;
      this.findUnitOfQuantityById(item.quantity);
      this.showItem.quantity=this.unitOfQuantity;
      return this.showItem;
    }
  */
  cookRecipe() {
    if (!Number.isInteger(this.numberOfPeople)) {
      this.notifications.pushFailure('Number of people has to be a whole number');
      return;
    }
    this.storageService.cookRecipe(this.recipe.id, this.numberOfPeople).subscribe({
      next: res => {
        this.items = null;
        this.deletedItems = res;
        this.notifications.pushSuccess('Recipe has been successfully cooked');
      },
      error: error => {
        console.error(error.message);
        this.notifications.pushFailure('Cooking failed, insufficient items in storage!');
      }
    });
  }

  planRecipe() {
    if (!Number.isInteger(this.numberOfPeople)) {
      this.notifications.pushFailure('Number of people has to be a whole number');
      return;
    }
    this.shoppingListService.planRecipe(this.recipe.id, this.numberOfPeople).subscribe({
      next: res => {
        this.items = res;
        this.notifications.pushSuccess('Ingredients has been successfully added to shopping list');
      },
      error: error => {
        console.error(error.message);
        this.notifications.pushFailure('Error while planning Recipe: ' + error.error.message);
      }
    });

  }

  putRecipeOnShoppingList() {
    if (!Number.isInteger(this.numberOfPeople)) {
      this.notifications.pushFailure('Number of people has to be a whole number');
      return;
    }
    this.shoppingListService.putRecipeOnShoppingList(this.recipe.id, this.numberOfPeople).subscribe({
      next: res => {
        this.items = res;
        this.notifications.pushSuccess('Ingredients has been successfully added to shopping list');
      },
      error: error => {
        console.error(error.message);
        this.notifications.pushFailure('Error while putting Items on Shopping List: ' + error.error.message);
      }
    });

  }


  findRecipeById(id: number) {
    this.recipeService.findRecipeById(id).subscribe({
      next: data => {
        console.log('received recipe', data);
        this.recipe = data;
      },
      error: error => {
        console.error(error.message);
        this.notifications.pushFailure('Error while finding Recipe: ' + error.error.message);
      }
    });
  }

  getValue(id: number) {
    let result: string;
    this.recipeService.findUnitOfQuantityById(id).subscribe(res => result = res);
    return result;
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

  addTempIngredient() {
    console.log('Add ingredient to temp recipe', this.tempIngredient, this.tempIngredient.quantity.name);
    if (this.recipeToUpdate.ingredients === null || this.recipeToUpdate.ingredients === undefined) {
      this.recipeToUpdate.ingredients = [];
    }
    this.tempIngredient.id = null;
    this.recipeToUpdate.ingredients.push(this.tempIngredient);
    this.tempIngredient = undefined;
  }

  updateRecipe(recipe: Recipe) {
    console.log('update Recipe', this.recipeToUpdate);
    this.recipeService.updateRecipe(recipe).subscribe({
      next: data => {
        console.log('received recipes', data);
        this.findRecipeById(this.recipe.id);
        this.notifications.pushSuccess('Recipe has been updated successfully');
      },
      error: error => {
        console.error(error.message);
        this.notifications.pushFailure('Error while updating Recipe: ' + error.error.message);
      }
    });
  }

  setRecipeToUpdate(recipe: Recipe) {
    this.recipeToUpdate.id = recipe.id;
    this.recipeToUpdate.name = recipe.name;
    this.recipeToUpdate.description = recipe.description;
    if (recipe.ingredients != null) {
      for (const ingredient of recipe.ingredients) {
        this.recipeToUpdate.ingredients.push(ingredient);
      }
    } else {
      this.recipeToUpdate.ingredients = null;
    }
    if (recipe.categories != null) {
      for (const categorie of recipe.categories) {
        this.recipeToUpdate.categories.push(categorie);
      }
    } else {
      this.recipeToUpdate.categories = null;
    }
    this.recipeToUpdate.groupId = recipe.groupId;
  }

  removeItem(item: Item) {
    for (let i = 0; i < this.recipeToUpdate.ingredients.length; i++) {
      if (this.recipeToUpdate.ingredients[i].id === item.id) {
        this.recipeToUpdate.ingredients.splice(i, 1);
      }
    }
  }

  openAddModal(recipeAddModal: TemplateRef<any>) {
    this.modalService.open(recipeAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  updateRecipeForm(form) {
    this.submitted = true;
    if (this.recipeToUpdate.ingredients === undefined || this.recipeToUpdate.ingredients === null) {
      this.error = 'Recipe needs ingredients';
    } else if (form.valid) {
      console.log('form item to add', this.recipeToUpdate);
      this.updateRecipe(this.recipeToUpdate);
      this.clearForm();
      this.vanishError();
    }
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

  findUnitOfQuantityById(id: number) {
    return this.recipeService.findUnitOfQuantityById(id);
  }

  private clearForm() {
    this.tempIngredient = undefined;
    this.recipeToUpdate = new Recipe();
    this.submitted = false;
  }

}
