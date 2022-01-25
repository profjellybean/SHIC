import {Injectable} from '@angular/core';
import {Globals} from '../global/globals';
import {HttpBackend, HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Register} from '../dtos/register';
import {TimeSumBill} from '../dtos/time-sum-bill';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  private registerBaseUri: string = this.globals.backendUri + '/register';
  private nakedHttpClient: HttpClient;
  private authHttpClient: HttpClient;

  constructor(private globals: Globals, handler: HttpBackend, private httpClient: HttpClient) {
    this.nakedHttpClient = new HttpClient(handler);
    this.authHttpClient = httpClient;
  }

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

  getBillSumGroup(): Observable<number> {
    console.log('Loading sum of Bills for the group');
    return this.httpClient.get<number>(this.registerBaseUri + '/billSumGroup');
  }

  getBillSumUser(): Observable<number> {

    console.log('Loading sum of Bills for the user ');
    return this.httpClient.get<number>(this.registerBaseUri + '/billSumUser');
  }

  editMonthlyBudget(budget: number): Observable<number> {
    console.log('Loading monthly sum of Bills');
    return this.httpClient.put<number>(this.registerBaseUri + '/monthlybudget?budget=' + budget, budget);
  }

  getSumOfMonthAndYear(date: string): Observable<TimeSumBill> {
    console.log('Load sum of specific month and year');
    return this.httpClient.get<TimeSumBill>(this.registerBaseUri + '/sumOfMonthAndYear' + '?date=' + date);
  }

  getSumOfYear(date: string): Observable<TimeSumBill> {
    console.log('Load sum of specific month and year');
    return this.httpClient.get<TimeSumBill>(this.registerBaseUri + '/sumOfYear' + '?date=' + date);
  }

}
