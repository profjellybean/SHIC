import { Injectable } from '@angular/core';

import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Item} from '../dtos/item';
import {UnitOfQuantity} from '../dtos/unitOfQuantity';


@Injectable({
  providedIn: 'root'
})
export class ItemService {

  private itemBaseUri: string = this.globals.backendUri + '/item';

  constructor(private httpClient: HttpClient,
              private globals: Globals) {
  }

  findAll(): Observable<Item[]>{
    console.log('load all items');
    return this.httpClient.get<Item[]>(this.itemBaseUri);
  }

  findAllItemsForGroup(): Observable<Item[]>{
    console.log('load all items for group');
    return this.httpClient.get<Item[]>(this.itemBaseUri + '/groupItems');
  }

  findAllItemsByGroupId(): Observable<Item[]>{
    console.log('load all items for group by groupId');
    return this.httpClient.get<Item[]>(this.itemBaseUri + '/groupItemsByGroupId');
  }

  editCustomItem(item: Item): Observable<Item> {
    console.log('edit item: ' + item);
    return this.httpClient.put<Item>(this.itemBaseUri + '/groupItems', item);
  }

  addItem(item: Item): Observable<Item> {
    console.log('add item: ' + item);
    return this.httpClient.post<Item>(this.itemBaseUri, item);

  }

  addCustomItem(item: Item): Observable<Item> {
    console.log('add custom item', item);
    return this.httpClient.post<Item>(this.itemBaseUri + '/groupItems', item);
  }

  createUnitOfQuantity(unityOfQuantity: string): Observable<UnitOfQuantity> {
    console.log('add unity of quantity: '+unityOfQuantity);
    return this.httpClient.post<UnitOfQuantity>(this.itemBaseUri+'/unitOfQuantity?name='+ unityOfQuantity, unityOfQuantity);
  }

}
