import { Component, OnInit } from '@angular/core';
import {Recipe} from '../../dtos/recipe';
import {RecipeService} from '../../services/recipe.service';

@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrls: ['./recipe.component.scss']
})
export class RecipeComponent implements OnInit {

  recipes: Recipe[] = null;

  error = false;
  errorMessage = '';

  constructor(
    private recipeService: RecipeService
  ) { }

  ngOnInit(): void {
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
