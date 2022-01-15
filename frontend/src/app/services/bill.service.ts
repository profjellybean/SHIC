import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {Bill} from '../dtos/bill';
import {HttpClient} from '@angular/common/http';
import {BillDto} from '../dtos/billDto';

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
    return this.httpClient.patch<Bill>(this.billBaseUri + '/?firstAdditionalId=' + billId +
      '&secondAdditionalId=' + userId, billId);
  }

  deleteBillById(id: number) {
    console.log('delete bill', id);
    return this.httpClient.delete(this.billBaseUri  + '/' + id);
  }

  bill(bill: BillDto): Observable<BillDto> {
    console.log('Add a new bill');
    return this.httpClient.post<BillDto>(this.billBaseUri, bill);
  }

  editBill(billToEdit: BillDto) {
    console.log('Edit a bill');
    return this.httpClient.put<BillDto>(this.billBaseUri, billToEdit);
  }
}
