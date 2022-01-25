import {Injectable} from '@angular/core';
import {HttpBackend, HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {ShoppingList} from '../dtos/shopping-list';
import {Item} from '../dtos/item';


@Injectable({
  providedIn: 'root'
})
export class ShoppingListService {

  private shoppingListBaseUri: string = this.globals.backendUri + '/shoppinglist';
  private nakedHttpClient: HttpClient;
  private authHttpClient: HttpClient;

  constructor(private globals: Globals, handler: HttpBackend, private httpClient: HttpClient) {
    this.nakedHttpClient = new HttpClient(handler);
    this.authHttpClient = httpClient;
  }

  getPrivateShoppingList(): Observable<ShoppingList> {
    console.log('service: get private shoppinglist');
    return this.httpClient.get<ShoppingList>(this.shoppingListBaseUri + '/private');
  }

  getPublicShoppingList(): Observable<ShoppingList> {
    console.log('service: get public shoppinglist');
    return this.httpClient.get<ShoppingList>(this.shoppingListBaseUri + '/public');
  }

  addToPrivateShoppingList(item: Item): Observable<Item> {
    console.log('service: add item to private shoppinglist with id: ', item.id);
    return this.httpClient.post<Item>(this.shoppingListBaseUri + '/private', item);
  }

  addToPublicShoppingList(item: Item): Observable<Item> {
    console.log('service: add item to public shoppinglist with id: ', item.id);
    return this.httpClient.post<Item>(this.shoppingListBaseUri + '/public', item);
  }

  changeAmountOfItemInPrivateShoppingList(item: Item): Observable<Item> {
    console.log('service: change amount of item on private shoppinglist with id: ', item.id);
    return this.httpClient.put<Item>(this.shoppingListBaseUri + '/private/' + item.id, item);
  }

  changeAmountOfItemInPublicShoppingList(item: Item): Observable<Item> {
    console.log('service: change amount of item on public shoppinglist with id: ', item.id);
    return this.httpClient.put<Item>(this.shoppingListBaseUri + '/public/' + item.id, item);
  }

  planRecipe(id: number, numOfPeople: number): Observable<Item[]> {
    console.log('service: plan recipe with id: ' + id + 'for number of people: ' + numOfPeople);
    //return this.httpClient.put<Item[]>(this.shoppingListBaseUri+'/?recipeId='+id+'&numberOfPeople='+numOfPeople, id);
    const httpParams = new HttpParams()
      .set('recipeId', id)
      .set('numberOfPeople', numOfPeople);
    return this.httpClient.put<Item[]>(this.shoppingListBaseUri, httpParams.toString(), {params: httpParams});
  }

  putRecipeOnShoppingList(id: number, numOfPeople: number): Observable<Item[]> {
    console.log('service: put all ingredients to shoppinglist of recipe with id: ' + id + 'for number of people: ' + numOfPeople);
    //return this.httpClient.put<Item[]>(
    //  this.shoppingListBaseUri+'/putAllIngredientsOfRecipe/?recipeId='+id+'&numberOfPeople='+numOfPeople, id);
    const httpParams = new HttpParams()
      .set('recipeId', id)
      .set('numberOfPeople', numOfPeople);
    return this.httpClient.put<Item[]>(
      this.shoppingListBaseUri + '/putAllIngredientsOfRecipe', httpParams.toString(), {params: httpParams});
  }

  /**
   * Loads items from the backend with specific parameters
   */
  searchItems(params: string): Observable<Item[]> {
    console.log('Search for items');
    return this.httpClient.get<Item[]>(
      this.shoppingListBaseUri + '/search' + params);
  }

  addItemToShoppingList(item: Item): Observable<Item> {
    console.log('service: add item to shoppinglist: ', item);
    console.log(this.shoppingListBaseUri + '/newItem');
    return this.httpClient.post<Item>(this.shoppingListBaseUri + '/newItem', item);
  }

  findAll(): Observable<Item[]> {
    console.log('service: load all items of shoppinglist');
    return this.httpClient.get<Item[]>(this.shoppingListBaseUri + '/availableItems');
  }

  findAllItems(): Observable<Item[]> {
    console.log('load items');
    return this.httpClient.get<Item[]>(this.shoppingListBaseUri + '/items');
  }

  workOffShoppingList(boughtItems: Item[], shoppinglistId: number): Observable<Item[]> {
    console.log('service: work off shopping-list: ' + boughtItems);
    return this.httpClient.put<Item[]>(this.shoppingListBaseUri + '/' + shoppinglistId, boughtItems);
  }

  getGroupStorageForUser(): Observable<number> {
    console.log('service: get group storage for user');
    return this.httpClient.get<number>(this.globals.backendUri + '/group/storage');
  }

  getGroupShoppingListForUser(): Observable<number> {
    console.log('service: get group shoppinglist for user');
    return this.httpClient.get<number>(this.globals.backendUri + '/group/shoppinglist');
  }

  deleteItemFromPrivate(id: number): Observable<object> {
    console.log('service: delete item of shoppinglist');
    return this.httpClient.delete<object>(this.shoppingListBaseUri + '/private/' + id);
  }

  deleteItemFromPublic(id: number): Observable<object> {
    console.log('service: delete item of shoppinglist');
    return this.httpClient.delete<object>(this.shoppingListBaseUri + '/public/' + id);
  }
}
