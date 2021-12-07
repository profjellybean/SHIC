import { Injectable } from '@angular/core';
import {Globals} from '../global/globals';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ItemStorage} from '../dtos/itemStorage';

@Injectable({
  providedIn: 'root'
})
export class ShoppinglistService {

  private shoppinglistBaseUri: string = this.globals.backendUri + 'shoppinglist';

  constructor(private httpClient: HttpClient,
              private globals: Globals
  ) { }

  addItemToShoppingList(itemStorage: ItemStorage): Observable<ItemStorage>{
    return null;
  }
}
