import {Injectable} from '@angular/core';
import {AuthRequest} from '../dtos/auth-request';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {tap} from 'rxjs/operators';
// @ts-ignore
import jwt_decode from 'jwt-decode';
import {Globals} from '../global/globals';
import {UserService} from './user.service';
import {User} from '../dtos/user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private user: User;
  private hasGroup = 0;

  private authBaseUri: string = this.globals.backendUri + '/authentication';

  constructor(private httpClient: HttpClient, private globals: Globals, private userService: UserService) {
  }

  /**
   * Login in the user. If it was successful, a valid JWT token will be stored
   *
   * @param authRequest User data
   */
  loginUser(authRequest: AuthRequest): Observable<string> {
    return this.httpClient.post(this.authBaseUri, authRequest, {responseType: 'text'})
      .pipe(
        tap((authResponse: string) => this.setToken(authResponse))
      );
  }


  /**
   * Check if a valid JWT token is saved in the localStorage
   */
  isLoggedIn() {
    return !!this.getToken() && (this.getTokenExpirationDate(this.getToken()).valueOf() > new Date().valueOf());
  }

  logoutUser() {
    console.log('Logout');
    localStorage.removeItem('authToken');
    this.user = undefined;
  }

  getToken() {
    return localStorage.getItem('authToken');
  }

  /**
   * Returns the user role based on the current token
   */
  getUserRole() {
    if (this.getToken() != null) {
      const decoded: any = jwt_decode(this.getToken());
      const authInfo: string[] = decoded.rol;
      if (authInfo.includes('ROLE_ADMIN')) {
        return 'ADMIN';
      } else if (authInfo.includes('ROLE_USER')) {
        return 'USER';
      }
    }
    return 'UNDEFINED';
  }

  hasCurrentGroup() {
    if(this.hasGroup === 0) {
      if (this.isLoggedIn()) {
        if (this.user === undefined) {
          // @ts-ignore
          this.userService.getCurrentUser({username: jwt_decode(this.getToken()).sub.trim()}).subscribe({
            next: data => {
              console.log('received items', data);
              this.user = data;
              if(this.user.currGroup !== null){
                this.hasGroup = 1;
              } else{
                this.hasGroup = -1;
              }
              return this.user.currGroup !== null;
            },
            error: error => {
              console.error(error.message);
            }
          });
        } else {
          return this.user.currGroup !== null;
        }
      } else {
        return false;
      }
    } else{
      return this.hasGroup === 1;
    }
  }

  private setToken(authResponse: string) {
    localStorage.setItem('authToken', authResponse);
  }

  private getTokenExpirationDate(token: string): Date {

    const decoded: any = jwt_decode(token);
    if (decoded.exp === undefined) {
      return null;
    }

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }
}
