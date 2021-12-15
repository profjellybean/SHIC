import {RegisterRequest} from '../dtos/RegisterRequest';
import {Observable} from 'rxjs';
import {HttpBackend, HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Injectable} from '@angular/core';
import {Username} from '../dtos/username';

@Injectable({
  providedIn: 'root'
})

export class UserService {



  private userRegisterUri: string = this.globals.backendUri + '/user';
  private httpClient: HttpClient;

  constructor(private globals: Globals, handler: HttpBackend) {
    this.httpClient = new HttpClient(handler);
  }

  confirmUser(confirmationToken: string): Observable<object> {

    return this.httpClient.get(this.userRegisterUri +'?confirm=' + confirmationToken);
  }

  resendConfirmation(username: Username): Observable<object> {
    return this.httpClient.put(this.userRegisterUri + '/confirmation', username);
  }
  registerUser(registerRequest: RegisterRequest): Observable<object> {

    return this.httpClient.post(this.userRegisterUri, registerRequest);
  }




}
