import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Recipe} from '../dtos/recipe';

@Injectable({
  providedIn: 'root'
})
export class RecipeService {

  private recipeBaseUri: string = this.globals.backendUri + '/recipe';

  constructor(private httpClient: HttpClient,
              private globals: Globals) {

  }

  findAll(): Observable<Recipe[]>{
    console.log('load recipes');
    return this.httpClient.get<Recipe[]>(this.recipeBaseUri);
  }

  findRecipeById(id: number): Observable<Recipe>{
    console.log('load recipe with id ' + 1);
    return this.httpClient.get<Recipe>(this.recipeBaseUri + '/' + id);
  }
  findUnitOfQuantityById(id: number){
    return this.httpClient.get<string>(this.globals.backendUri+'/item/unitOfQuantity/byId?id='+ id);
  }

  addRecipe(recipe: Recipe): Observable<Recipe> {
    console.log('addRecipe, Service', recipe);
    return this.httpClient.post<Recipe>(this.recipeBaseUri, recipe);
  }

  updateRecipe(recipe: Recipe): Observable<Recipe> {
    console.log('update Recipe, Service', recipe);
    return this.httpClient.put<Recipe>(this.recipeBaseUri + '/' + recipe.id, recipe);
  }
}
