import { Injectable } from '@angular/core';

import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Item} from '../dtos/item';


@Injectable({
  providedIn: 'root'
})
export class ItemService {

  private recipeBaseUri: string = this.globals.backendUri + '/item';

  constructor(private httpClient: HttpClient,
              private globals: Globals) {
  }

  findAll(): Observable<Item[]>{
    console.log('load all items');
    return this.httpClient.get<Item[]>(this.recipeBaseUri);
  }

  findAllItemsForGroup(): Observable<Item[]>{
    console.log('load all items for group');
    return this.httpClient.get<Item[]>(this.recipeBaseUri + '/groupItems');
  }

  addItem(item: Item): Observable<Item> {
    console.log('add item: ' + item);
    return this.httpClient.post<Item>(this.recipeBaseUri, item);

  }

}
