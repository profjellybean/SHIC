import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
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

  findAllByName(name: string): Observable<Recipe[]>{
    console.log('load recipes with name', name);
    return this.httpClient.get<Recipe[]>(this.recipeBaseUri + '/findbyname?name=' + name);
  }

  findRecipeById(id: number): Observable<Recipe>{
    console.log('load recipe with id ' + 1);
    return this.httpClient.get<Recipe>(this.recipeBaseUri + '/' + id);
  }
  deleteRecipeById(id: number): Observable<boolean>{
    console.log('delete recipe with id '+ id);
    return this.httpClient.delete<boolean>(this.recipeBaseUri+ '/' + id);
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
