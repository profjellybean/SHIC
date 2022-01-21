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
  items; // = [];
  deletedItems;
  ingredientsShow: ShowItem[];
  showItem: ShowItem;
  submitted = false;
  tempIngredient: Item;
  allItems: Item[];
  numberOfPeople = 1;


  error = false;
  errorMessage = '';
  private expDate: number;

  constructor(private recipeService: RecipeService,
              private route: ActivatedRoute,
              private shoppingListService: ShoppingListService,
              private modalService: NgbModal,
              private itemService: ItemService,
              private storageService: StorageService,
              private notfication: NotificationsComponent) {
  }

  ngOnInit(): void {
    this.recipe.id = this.route.snapshot.params.id;
    this.findRecipeById(this.recipe.id);
    this.getAllItems();
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
  cookRecipe(){
    this.storageService.cookRecipe(this.recipe.id, this.numberOfPeople).subscribe({
      next: res => {
        this.items = null;
        this.deletedItems = res;
      },
      error: err => {
        this.notfication.pushFailure('Cooking failed, insufficient items in storage!');
      }
    });
  }

  planRecipe() {
    this.shoppingListService.planRecipe(this.recipe.id, this.numberOfPeople).subscribe({
      next: res => {
        this.items = res;
      },
      error: err => {
        this.defaultServiceErrorHandling(err);
      }
    });

  }

  putRecipeOnShoppingList() {
    this.shoppingListService.putRecipeOnShoppingList(this.recipe.id, this.numberOfPeople).subscribe({
      next: res => {
        this.items = res;
      },
      error: err => {
        this.defaultServiceErrorHandling(err);
      }
    });

  }


  findRecipeById(id: number) {
    this.recipeService.findRecipeById(id).subscribe({
      next: data => {
        console.log('received recipes', data);
        this.recipe = data;
      },
      error: err => {
        this.defaultServiceErrorHandling(err);
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
        this.defaultServiceErrorHandling(error);
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
      },
      error: error => {
        this.defaultServiceErrorHandling(error);
      }
    });
  }

  setRecipeToUpdate(recipe: Recipe) {
    this.recipeToUpdate.id = recipe.id;
    this.recipeToUpdate.name = recipe.name;
    this.recipeToUpdate.description = recipe.description;
    this.recipeToUpdate.ingredients = recipe.ingredients;
    this.recipeToUpdate.categories = recipe.categories;
    this.recipeToUpdate.groupId = recipe.groupId;
    console.log('I am set ' + this.recipeToUpdate.name);
  }

  removeItem(item: Item) {
    console.log('now i will delete you ' + this.recipeToUpdate.ingredients);
    for (let i = 0; i < this.recipeToUpdate.ingredients.length; i++) {
      if(this.recipeToUpdate.ingredients[i].id === item.id) {
        this.recipeToUpdate.ingredients.splice(i, 1);
      }
    }
  }

  openAddModal(recipeAddModal: TemplateRef<any>) {
    this.modalService.open(recipeAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  updateRecipeForm(form) {
    this.submitted = true;
    if(this.recipeToUpdate.ingredients === undefined || this.recipeToUpdate.ingredients === null){
      this.error = true;
    } else if (form.valid) {
      //this.storageService.addItem(this.item);
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
    this.error = false;
  }

  findUnitOfQuantityById(id: number) {
    return this.recipeService.findUnitOfQuantityById(id);
  }

  private clearForm() {
    this.tempIngredient = undefined;
    this.recipeToUpdate = new Recipe();
    this.submitted = false;
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      this.notfication.pushFailure(error.error.error);
    } else {
      this.notfication.pushFailure(error.error);
    }
  }

}
