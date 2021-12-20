import { Component, OnInit } from '@angular/core';
import {Recipe} from '../../dtos/recipe';
import {Item} from '../../dtos/item';
import {RecipeService} from '../../services/recipe.service';
import {ActivatedRoute} from '@angular/router';
import {ShoppingListService} from '../../services/shopping-list.service';
import {elementAt, Observable} from 'rxjs';
import {UnitOfQuantity} from '../../dtos/unitOfQuantity';
import {ShowItem} from '../../dtos/ShowItem';

@Component({
  selector: 'app-recipe-detail',
  templateUrl: './recipe-detail.component.html',
  styleUrls: ['./recipe-detail.component.scss']
})
export class RecipeDetailComponent implements OnInit {

  recipe: Recipe = {
    id: null, name: null, description: null, ingredients: [], categories: []
  };
  name: string;
  quantities: number[];
  unitOfQuantity: string;
  ingredients: Item[];
  items; // = [];
  ingredientsShow: ShowItem[];
  showItem: ShowItem;


  error = false;
  errorMessage = '';
  private expDate: number;

  constructor( private recipeService: RecipeService,
              private route: ActivatedRoute,
              private shoppingListService: ShoppingListService) { }

  ngOnInit(): void {
    this.recipe.id = this.route.snapshot.params.id;
    this.findRecipeById(this.recipe.id);
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




  planRecipe() {
    this.shoppingListService.planRecipe(this.recipe.id).subscribe({
      next: res => {
        // TODO add success
        //this.recipe.name = 'test successful: '+res.name;
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
    this.recipeService.findUnitOfQuantityById(id).subscribe(res=> result= res);
    return result;
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }
  findUnitOfQuantityById(id: number){
    return this.recipeService.findUnitOfQuantityById(id);
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
