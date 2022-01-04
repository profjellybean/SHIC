import {Component, OnInit, TemplateRef} from '@angular/core';
import {Recipe} from '../../dtos/recipe';
import {RecipeService} from '../../services/recipe.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Item} from '../../dtos/item';
import {ItemService} from '../../services/item.service';

@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrls: ['./recipe.component.scss']
})
export class RecipeComponent implements OnInit {

  recipes: Recipe[] = null;
  ingredientsToAdd: Item[] = null;
  nullRecipe: Recipe = {name: null, id: null, categories: null, description: null, ingredients: this.ingredientsToAdd};

  recipeToAdd = this.nullRecipe;
  error = false;
  errorMessage = '';
  submitted = false;
  allItems: Item[];
  tempIngredient: Item;

  constructor(
    private recipeService: RecipeService,
    private modalService: NgbModal,
    private itemService: ItemService
  ) {
  }

  ngOnInit(): void {
    this.reloadRecipes();
    this.getAllItems();
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
        this.defaultServiceErrorHandling(error);
      }
    });
  }


  openAddModal(recipeAddModal: TemplateRef<any>) {
    this.modalService.open(recipeAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  addRecipeForm(form) {
    this.submitted = true;
    if(this.recipeToAdd.ingredients === undefined || this.recipeToAdd.ingredients === null){
      this.error = true;
    } else if (form.valid) {
      //this.storageService.addItem(this.item);
      console.log('form item to add', this.recipeToAdd);
      this.addRecipe(this.recipeToAdd);
      this.clearForm();
      this.vanishError();
    }
  }

  addRecipe(recipe: Recipe) {
    console.log('addRecipe', this.recipeToAdd);
    this.recipeService.addRecipe(recipe).subscribe({
      next: data => {
        console.log('received recipes', data);
        this.reloadRecipes();
      },
      error: error => {
        this.defaultServiceErrorHandling(error);
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
        this.defaultServiceErrorHandling(error);
      }
    });
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }

  private clearForm() {
    this.tempIngredient = undefined;
    this.recipeToAdd = new Recipe();
    this.submitted = false;
  }
}
