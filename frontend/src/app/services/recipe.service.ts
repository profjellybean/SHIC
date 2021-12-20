import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Recipe} from '../dtos/recipe';
import {ShoppingList} from '../dtos/shopping-list';
import {UnitOfQuantity} from '../dtos/unitOfQuantity';

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
}
