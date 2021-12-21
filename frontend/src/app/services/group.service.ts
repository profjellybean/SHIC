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
  private httpClient: HttpClient;

  constructor(private globals: Globals, handler: HttpBackend) {
    this.httpClient = new HttpClient(handler);
  }

  generateGroup(): Observable<any> {
    return this.httpClient.post(this.userRegisterUri, null);
  }


  addUser(username: string, groupId: number) {
    return this.httpClient.put(this.userRegisterUri, {}, {params:{username, groupId}});
  }

  getAllUsers(groupId: number): Observable<User[]> {
    // @ts-ignore
    return this.httpClient.get(this.userRegisterUri, {params: {groupId}});
  }
}
