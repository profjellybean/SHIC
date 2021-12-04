import { Injectable } from '@angular/core';
import {Globals} from '../global/globals';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Register} from '../dtos/register';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  private registerBaseUri: string = this.globals.backendUri + '/register';

  constructor(private httpClient: HttpClient, private globals: Globals) { }

  confirmPayment(billId: number, userId: number, register: Register): Observable<Register> {
    console.log('Confirm Payment with bill id ' + billId + ' and user id ' + userId);
    return this.httpClient.put<Register>(this.registerBaseUri + '/' + billId + '/' + userId, register);
  }
}
