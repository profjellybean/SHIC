import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Recipe} from '../dtos/recipe';

@Injectable({
  providedIn: 'root'
})
export class RecipeService {

  private messageBaseUri: string = this.globals.backendUri + '/recipe';

  constructor(private httpClient: HttpClient,
              private globals: Globals) {
  }

  findAll(): Observable<Recipe[]>{
    return null;
  }
}
