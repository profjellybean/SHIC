import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {ShoppingList} from '../dtos/shopping-list';
import {Item} from '../dtos/item';
import {Username} from '../dtos/username';


@Injectable({
  providedIn: 'root'
})
export class ShoppingListService {

  private shoppingListBaseUri: string = this.globals.backendUri + '/shoppinglist';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  getPrivateShoppingList(): Observable<ShoppingList> {
    console.log('get shoppinglist with id: ');
    return this.httpClient.get<ShoppingList>(this.shoppingListBaseUri + '/private');
  }

  getPublicShoppingList(): Observable<ShoppingList> {
    console.log('get shoppinglist with id: ');
    return this.httpClient.get<ShoppingList>(this.shoppingListBaseUri + '/public');
  }

  addToPrivateShoppingList(item: Item): Observable<ShoppingList> {
    console.log('get shoppinglist with id: ');
    return this.httpClient.post<ShoppingList>(this.shoppingListBaseUri + '/private', item);
  }

  addToPublicShoppingList(item: Item): Observable<ShoppingList> {
    console.log('get shoppinglist with id: ');
    return this.httpClient.post<ShoppingList>(this.shoppingListBaseUri + '/public', item);
  }

  planRecipe(id: number): Observable<Item[]> {
    console.log('plan recipe with id: ' + id);
    return this.httpClient.put<Item[]>(this.shoppingListBaseUri+'/?recipeId='+id, id);
  }

  addItemToShoppingList(item: Item): Observable<Item>{
    console.log('service ', item);
    console.log(this.shoppingListBaseUri + '/newItem');
    return this.httpClient.post<Item>(this.shoppingListBaseUri + '/newItem', item);
  }

  findAll(id: number): Observable<Item[]>{
    console.log('load items of shoppinglist');
    return this.httpClient.get<Item[]>(this.shoppingListBaseUri + '/availableItems');
  }

  findAllItems(): Observable<Item[]>{
    console.log('load items');
    return this.httpClient.get<Item[]>(this.shoppingListBaseUri + '/items');
  }

  workOffShoppingList(boughtItems: Item[], shoppinglistId: number): Observable<Item[]> {
    console.log('work off shopping-list: ' + boughtItems);
    return this.httpClient.put<Item[]>(this.shoppingListBaseUri + '/' + shoppinglistId, boughtItems);
  }
}
