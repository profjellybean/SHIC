import { Component, OnInit } from '@angular/core';
import {MessageService} from '../../services/message.service';
import {Recipe} from '../../dtos/recipe';
import {RecipeService} from '../../services/recipe.service';
import {ActivatedRoute} from '@angular/router';
import {ShoppingListService} from '../../services/shopping-list.service';

@Component({
  selector: 'app-recipe-detail',
  templateUrl: './recipe-detail.component.html',
  styleUrls: ['./recipe-detail.component.scss']
})
export class RecipeDetailComponent implements OnInit {

  recipe: Recipe = {
    id: null, name: null, description: null
  };

  error = false;
  errorMessage = '';

  constructor(private messageService: MessageService, private recipeService: RecipeService,
              private route: ActivatedRoute,
              private shoppingListService: ShoppingListService) { }

  ngOnInit(): void {
    this.recipe.id = this.route.snapshot.params.id;
    this.findRecipeById(this.recipe.id);
  }


  planRecipe() {
    this.shoppingListService.planRecipe(this.recipe.id).subscribe({
      next: res => {
        // TODO add success
        this.recipe.name = 'test successful: '+res.name;
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
