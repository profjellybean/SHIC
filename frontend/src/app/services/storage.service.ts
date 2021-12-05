import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {Item} from '../dtos/item';
import {Params} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  private storageBaseUri: string = 'http://localhost:8080/storage';

  constructor(private httpClient: HttpClient, private globals: Globals) {
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
}
