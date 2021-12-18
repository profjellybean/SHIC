import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {ShoppingList} from '../dtos/shopping-list';
import {Item} from '../dtos/item';
import {ItemStorage} from '../dtos/itemStorage';


@Injectable({
  providedIn: 'root'
})
export class ShoppingListService {

  private shoppingListBaseUri: string = this.globals.backendUri + '/shoppinglist';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  getShoppingList(): Observable<string> {
    console.log('get shoppinglist with id: ');
    return this.httpClient.get<string>(this.shoppingListBaseUri);
  }


  // TODO get user id
  planRecipe(id: number): Observable<Item[]> {
    console.log('plan recipe with id: ' + id);
    return this.httpClient.put<Item[]>(this.shoppingListBaseUri+'/?recipeId='+id+'&userId=1', id);
  }

  addItemToShoppingList(item: Item): Observable<Item>{
    console.log('service ', item);
    console.log(this.shoppingListBaseUri + '/newItem');
    return this.httpClient.post<Item>(this.shoppingListBaseUri + '/newItem', item);
  }

  findAll(id: number): Observable<Item[]>{
    console.log('load items of shoppinglist');

    const params = new HttpParams()
      .set('id', id);
    return this.httpClient.get<Item[]>(this.shoppingListBaseUri + '/shoppingListItems', {params});
  }

  findAllItems(): Observable<Item[]>{
    console.log('load items');
    return this.httpClient.get<Item[]>(this.shoppingListBaseUri + '/items');
  }

  workOffShoppingList(boughtItems: Item[]): Observable<Item[]> {
    console.log('work off shopping-list: ' + boughtItems);
    return this.httpClient.put<Item[]>(this.shoppingListBaseUri + boughtItems.toString(), boughtItems);
  }
}
