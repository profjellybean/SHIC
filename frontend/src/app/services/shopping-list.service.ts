import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class ShoppingListService {

  private messageBaseUri: string = this.globals.backendUri + '/shoppinglist';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }
}
