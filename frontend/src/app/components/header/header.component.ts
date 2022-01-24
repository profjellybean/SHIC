import {Component, Injectable} from '@angular/core';
import {AuthService} from '../../services/auth.service';

@Injectable({
  providedIn: 'root'
})

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  static username = 'User';
  private hasCurrentGroupBool: boolean;
  private hasCurrentGroupChecked: boolean;


  constructor(public authService: AuthService) {
  }


  public hasCurrentGroup(): boolean {
    return localStorage.getItem('currGroup') === 'true';
  }

  setUsername(username: string) {
    HeaderComponent.username = username;
  }

  getUsername(): string {
    return localStorage.getItem('username');
  }
}

