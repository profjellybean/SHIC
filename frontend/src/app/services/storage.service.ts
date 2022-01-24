import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {Item} from '../dtos/item';
import {Params} from '@angular/router';
import {UnitOfQuantity} from '../dtos/unitOfQuantity';
import {TimeSumBill} from '../dtos/time-sum-bill';
import {NameSum} from '../dtos/name-sum';

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

  cookRecipe(id: number, numOfPeople: number){
    console.log('service: cook recipe with id: ' + id + 'for number of people: ' + numOfPeople);
    const httpParams = new HttpParams()
      .set('recipeId', id)
      .set('numberOfPeople', numOfPeople);
    return this.httpClient.put<Item[]>(this.storageBaseUri+'/recipe', httpParams.toString(), {params: httpParams});
  }

  updateItem(item: Item): Observable<Item> {
    return this.httpClient.put<Item>(this.storageBaseUri, item);
  }

  findAllUnitsOfQuantity(): Observable<UnitOfQuantity[]>{
    console.log('load UnitOfQuantity');
    return this.httpClient.get<UnitOfQuantity[]>(this.storageBaseUri + '/unitOfQuantity');
  }

  deleteItemFromStorage(params: Params): Observable<Item> {
    console.log('service: delete item from storage');
    return this.httpClient.delete<Item>(this.storageBaseUri, {params});
  }
  getSumOfArticlesOfSpecificYear(date: string): Observable<TimeSumBill>{
    console.log('Load sum of articles of specific year');
    return this.httpClient.get<TimeSumBill>(this.storageBaseUri + '/thrownAwayInSpecificYear'+'?date='+date);
  }
  getSumOfArticlesOfSpecificMonth(date: string): Observable<TimeSumBill>{
    console.log('Load sum of articles of specific month');
    return this.httpClient.get<TimeSumBill>(this.storageBaseUri + '/thrownAwayInSpecificMonth'+'?date='+date);
  }
  getMostThrownAwayArticles(): Observable<NameSum[]>{
    console.log('get most often thrown away articles');
    return this.httpClient.get<NameSum[]>(this.storageBaseUri+ '/mostOftenThrownAwayArticles');
  }
}
