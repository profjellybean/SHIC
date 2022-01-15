import {RegisterRequest} from '../dtos/RegisterRequest';
import {Observable} from 'rxjs';
import {HttpBackend, HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Injectable} from '@angular/core';
import {Username} from '../dtos/username';
import {User} from '../dtos/user';
import {Params} from '@angular/router';
import {Image} from '../dtos/image';

@Injectable({
  providedIn: 'root'
})

export class UserService {



  private userRegisterUri: string = this.globals.backendUri + '/user';
  private nakedHttpClient: HttpClient;
  private authHttpClient: HttpClient;

  constructor(private globals: Globals, handler: HttpBackend, private httpClient: HttpClient ) {
    this.nakedHttpClient = new HttpClient(handler);
    this.authHttpClient = httpClient;
  }

  confirmUser(confirmationToken: string): Observable<object> {

    return this.nakedHttpClient.get(this.userRegisterUri +'/confirm?tkn=' + confirmationToken);
  }

  resendConfirmation(username: Username): Observable<object> {
    return this.nakedHttpClient.put(this.userRegisterUri + '/confirmation', username);
  }
  registerUser(registerRequest: RegisterRequest): Observable<object> {

    return this.nakedHttpClient.post(this.userRegisterUri, registerRequest);
  }

  editUsername(newUsername: string): Observable<object> {
    return this.authHttpClient.put<object>(this.userRegisterUri, {username: newUsername});
  }

  editPicture(image: any): Observable<Image> {
    const formData = new FormData();
    formData.append('file', image);

    return this.authHttpClient.put<Image>(this.userRegisterUri+'/picture', formData);
  }

  changeEmail(newEmail: string): Observable<object> {
    return this.authHttpClient.put<object>(this.userRegisterUri+'/email', {email: newEmail});
  }


  getCurrentUser(params: Params): Observable<User>{
    console.log('get user');
    return this.authHttpClient.get<User>(this.userRegisterUri);
  }

  deleteUserById(id: number) {
    console.log('delete user', id);
    return this.nakedHttpClient.delete(this.userRegisterUri  + '/' + id);
  }
}
