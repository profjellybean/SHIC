import { Injectable } from '@angular/core';

import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Item} from '../dtos/Item';


@Injectable({
  providedIn: 'root'
})
export class ItemService {

  private recipeBaseUri: string = this.globals.backendUri + '/item';

  constructor(private httpClient: HttpClient,
              private globals: Globals) {
  }

}
