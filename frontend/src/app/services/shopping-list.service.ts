import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {ShoppingList} from '../dtos/shopping-list';

@Injectable({
  providedIn: 'root'
})
export class ShoppingListService {

  private shoppinListBaseUri: string = this.globals.backendUri + '/shoppinglist';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }


  // TODO get user id
  planRecipe(id: number): Observable<ShoppingList> {
    console.log('plan recipe with id: ' + id);
    return this.httpClient.put<ShoppingList>(this.shoppinListBaseUri+'/?recipeId='+id+'&storageId='+1, id);
  }
}
