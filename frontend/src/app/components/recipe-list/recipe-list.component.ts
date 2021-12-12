import { Component, OnInit } from '@angular/core';
import {RecipeService} from '../../services/recipe.service';
import {Recipe} from '../../dtos/recipe';

@Component({
  selector: 'app-recipe-list',
  templateUrl: './recipe-list.component.html',
  styleUrls: ['./recipe-list.component.scss']
})
export class RecipeListComponent implements OnInit {

  recipes: Recipe[] = null;

  error = false;
  errorMessage = '';

  constructor(
    private recipeService: RecipeService
  ) { }

  ngOnInit(): void {
    this.reloadRecipes();
  }

  reloadRecipes() {
    this.recipeService.findAll().subscribe({
      next: data => {
        console.log('received recipes', data);
        this.recipes = data;
      }
    });
  }

}
