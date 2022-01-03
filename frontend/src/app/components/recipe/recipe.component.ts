import {Component, OnInit, TemplateRef} from '@angular/core';
import {Recipe} from '../../dtos/recipe';
import {RecipeService} from '../../services/recipe.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Item} from '../../dtos/item';
import {ItemService} from "../../services/item.service";

@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrls: ['./recipe.component.scss']
})
export class RecipeComponent implements OnInit {

  recipes: Recipe[] = null;
  nullRecipe: Recipe = {name: null, id: null, categories: null, description: null, ingredients: null};

  recipeToAdd = this.nullRecipe;
  error = false;
  errorMessage = '';
  submitted = false;
  allItems: Item[];

  constructor(
    private recipeService: RecipeService,
    private modalService: NgbModal,
    private itemService: ItemService
  ) {
  }

  ngOnInit(): void {
    this.getAllItems();
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



  openAddModal(itemAddModal: TemplateRef<any>) {
    //this.item = new Item();
    this.modalService.open(itemAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  addRecipeForm(form) {
    this.submitted = true;

    if (form.valid) {
      //this.storageService.addItem(this.item);
      console.log('form item to add', this.recipeToAdd);
      this.addRecipe(this.recipeToAdd);
      this.clearForm();
    }
  }

  addRecipe(recipe: Recipe) {

  }

  private clearForm() {
    //this.item = new Item();
    this.recipeToAdd = new Recipe();
    this.submitted = false;
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
}
