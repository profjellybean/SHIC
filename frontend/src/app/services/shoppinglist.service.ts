
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class ShoppingListService {


  private shoppingListUri = 'http://localhost:8080/shoppinglist';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  getShoppingList(): Observable<string> {

    return this.httpClient.get<string>(this.shoppingListUri);
  }




}
