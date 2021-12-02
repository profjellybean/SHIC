import { Component, OnInit } from '@angular/core';
import {MessageService} from '../../services/message.service';
import {Recipe} from '../../dtos/recipe';
import {RecipeService} from '../../services/recipe.service';

@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrls: ['./recipe.component.scss']
})
export class RecipeComponent implements OnInit {

  fakeRecipe: Recipe = {
    id: 1, name: 'fakeRecipe', description: 'this recipe is not real'
  };
  recipes: Recipe[] = null;

  error = false;
  errorMessage = '';

  constructor(
    private messageService: MessageService,
    private recipeService: RecipeService
  ) { }

  ngOnInit(): void {
  }

  reloadRecipes() {
    this.recipeService.findAll().subscribe({
      next: data => {
        console.log('received recipes', data);
        this.recipes = data;
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
