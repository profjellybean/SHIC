import {Observable} from 'rxjs';
import {HttpBackend, HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Injectable} from '@angular/core';
import {User} from '../dtos/user';


@Injectable({
  providedIn: 'root'
})

export class GroupService {


  private userRegisterUri = this.globals.backendUri + '/group';
  private nakedHttpClient: HttpClient;
  private authHttpClient: HttpClient;

  constructor(private globals: Globals, handler: HttpBackend, private httpClient: HttpClient) {
    this.nakedHttpClient = new HttpClient(handler);
    this.authHttpClient = httpClient;
  }

  generateGroup(groupName: string, userName: string): Observable<any> {
    return this.httpClient.post(this.userRegisterUri, null, {params: {groupName, userName}});
  }


  addUser(username: string, groupId: number) {
    return this.httpClient.put(this.userRegisterUri, null, {params: {username, groupId}});
  }

  getAllUsers(groupId: number): Observable<User[]> {
    // @ts-ignore
    return this.httpClient.get(this.userRegisterUri, {params: {groupId}});
  }


}
