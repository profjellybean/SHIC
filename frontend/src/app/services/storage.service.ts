import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {Item} from '../dtos/item';
import {ItemStorage} from '../dtos/itemStorage';
import {Params} from '@angular/router';
import {UnitOfQuantity} from '../dtos/unitOfQuantity';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  private storageBaseUri: string = this.globals.backendUri + '/storage';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads items from the backend with specific parameters
   */
  searchItems(params: string): Observable<Item[]> {
    console.log('Search for items');
    return this.httpClient.get<Item[]>(
      this.storageBaseUri+'/search'+params);
  }


  /**
   * Loads all items from the backend
   */
  getItems(params: Params): Observable<Item[]> {
    console.log('Get all items');
    return this.httpClient.get<Item[]>(this.storageBaseUri, {params});
  }
  /**
   * Persists message to the backend
   *
   * @param item to persist
   */
  createMessage(item: Item): Observable<Item> {
    console.log('Create item');
    return this.httpClient.post<Item>(this.storageBaseUri, item);
  }

  addItem(item: Item): Observable<Item> {
    return this.httpClient.post<Item>(this.storageBaseUri, item);
  }

  findAllUnitsOfQuantity(): Observable<UnitOfQuantity[]>{
    console.log('load UnitOfQuantity');
    return this.httpClient.get<UnitOfQuantity[]>(this.storageBaseUri + '/unitOfQuantity');
  }
}
