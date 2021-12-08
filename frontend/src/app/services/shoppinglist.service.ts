
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Injectable} from '@angular/core';
import {ItemStorage} from '../dtos/itemStorage';

@Injectable({
  providedIn: 'root'
})




export class ShoppingListService {

  private shoppinglistBaseUri: string = this.globals.backendUri + 'shoppinglist';

  constructor(private httpClient: HttpClient,
              private globals: Globals
  ) { }

  addItemToShoppingList(itemStorage: ItemStorage): Observable<ItemStorage>{
    return null;
  }

  getShoppingList(): Observable<string> {

    return this.httpClient.get<string>(this.shoppinglistBaseUri);
  }
}
