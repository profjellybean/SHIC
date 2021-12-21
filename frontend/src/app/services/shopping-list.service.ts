import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {ShoppingList} from '../dtos/shopping-list';
import {Item} from '../dtos/item';


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

  addToPrivateShoppingList(item: Item): Observable<Item> {
    console.log('get shoppinglist with id: ');
    return this.httpClient.post<Item>(this.shoppingListBaseUri + '/private', item);
  }

  addToPublicShoppingList(item: Item): Observable<Item> {
    console.log('get shoppinglist with id: ');
    return this.httpClient.post<Item>(this.shoppingListBaseUri + '/public', item);
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

  findAll(): Observable<Item[]>{
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

  getGroupStorageForUser(): Observable<number>{
    console.log('get group storage for user');
    return this.httpClient.get<number>(this.globals.backendUri+ '/group/storage');
  }

  getGroupShoppingListForUser(): Observable<number>{
    console.log('get group shoppinglist for user');
    return this.httpClient.get<number>(this.globals.backendUri+ '/group/shoppinglist');
  }

  deleteItemFromPrivate(id: number): Observable<object>{
    console.log('delete item of shoppinglist');
    return this.httpClient.delete<object>(this.shoppingListBaseUri + '/private/'+id);
  }

  deleteItemFromPublic(id: number): Observable<object>{
    console.log('delete item of shoppinglist');
    return this.httpClient.delete<object>(this.shoppingListBaseUri + '/public/'+id);
  }
}
