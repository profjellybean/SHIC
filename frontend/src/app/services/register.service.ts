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

  getRegisterById(id: number): Observable<Register> {
    console.log('Load register details for ' + id);
    return this.httpClient.get<Register>(this.registerBaseUri + '/' + id);
  }

  confirmPayment(billId: number, username: string, registerId: number): Observable<Register> {
    console.log('Confirm Payment with bill id ' + billId + ' and user id ' + username);
    return this.httpClient.put<Register>(this.registerBaseUri + '/?id=' + registerId + '&additionalId=' + billId +
      '&additionalString=' + username, registerId);
  }

  getMonthlySum(): Observable<number> {
    console.log('Loading monthly sum of Bills');
    return this.httpClient.get<number>(this.registerBaseUri + '/monthlysum');
  }
}
