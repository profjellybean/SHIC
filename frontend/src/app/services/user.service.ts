import {RegisterRequest} from '../dtos/RegisterRequest';
import {Observable} from 'rxjs';
import {HttpBackend, HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class UserService {


  private userRegisterUri = 'http://localhost:8080/user';
  private httpClient: HttpClient;

  constructor(private globals: Globals, handler: HttpBackend) {
    this.httpClient = new HttpClient(handler);
  }

  registerUser(registerRequest: RegisterRequest): Observable<object> {


    return this.httpClient.post(this.userRegisterUri, registerRequest);
  }




}