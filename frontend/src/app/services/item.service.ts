import { Injectable } from '@angular/core';

import {HttpBackend, HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Item} from '../dtos/item';
import {UnitOfQuantity} from '../dtos/unitOfQuantity';
import {Params} from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class ItemService {

  private itemBaseUri: string = this.globals.backendUri + '/item';
  private nakedHttpClient: HttpClient;
  private authHttpClient: HttpClient;

  constructor(private globals: Globals, handler: HttpBackend, private httpClient: HttpClient ) {
    this.nakedHttpClient = new HttpClient(handler);
    this.authHttpClient = httpClient;
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

  searchItemsByName(searchName: string): Observable<Item[]>{
    console.log('load all items for group by groupId and name');
    return this.httpClient.get<Item[]>(this.itemBaseUri + '/groupItemsByGroupIdAndName/'+searchName);
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
