import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss']
})
export class HomepageComponent implements OnInit {

  static username = 'User';
  private hasCurrentGroupBool: boolean;
  private hasCurrentGroupChecked: boolean;

  constructor(public authService: AuthService) {
  }

  ngOnInit(): void {
  }

  public hasCurrentGroup(): boolean {
    if (!this.hasCurrentGroupChecked) {
      this.hasCurrentGroupBool = this.authService.hasCurrentGroup();
      this.hasCurrentGroupChecked = true;
    }
    return this.hasCurrentGroupChecked;

  }

  setUsername(username: string) {
    HomepageComponent.username = username;
  }

  getUsername(): string {
    return HomepageComponent.username;
  }
}
