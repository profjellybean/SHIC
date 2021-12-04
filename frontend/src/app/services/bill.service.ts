import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {Bill} from '../dtos/bill';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class BillService {

  private billBaseUri: string = this.globals.backendUri + '/bill';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads specific message from the backend
   *
   * @param id of message to load
   */
  getBillById(id: number): Observable<Bill> {
    console.log('Load bill details for ' + id);
    return this.httpClient.get<Bill>(this.billBaseUri + '/' + id);
  }

  getAllBills(): Observable<Bill[]> {
    console.log('get all bills');
    return this.httpClient.get<Bill[]>(this.billBaseUri);
  }

  deleteName(billId: number, userId: number): Observable<Bill> {
    console.log('delete user ' + userId + ' of bill ' + billId);
    return this.httpClient.delete<Bill>(this.billBaseUri);
  }
}
