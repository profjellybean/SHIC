import { Component, OnInit } from '@angular/core';
import {GroupService} from '../../services/group.service';
import {AuthService} from '../../services/auth.service';
import {UserService} from '../../services/user.service';
import jwt_decode from 'jwt-decode';
import {User} from '../../dtos/user';


@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {
  groupId = null;
  userToAdd: string;
  error: string;


  user: User = {
    // @ts-ignore
    username: jwt_decode(this.authService.getToken()).sub.trim(),
    password: null,
    id: null,
    currGroup: null,
    privList: null
  };

  constructor(private groupService: GroupService, public authService: AuthService, private userService: UserService) { }

  ngOnInit(): void {
    this.getCurrentGroup();
    console.log(this.groupId);
  }

  generateGroup(){
    this.groupService.generateGroup().subscribe({
      next: data => {
        console.log('received items', data);
        this.groupId = data;
      },
      error: error => {
        console.error(error.message);
      }
    });
  }

  getCurrentGroup(){
    this.userService.getCurrentUser({username: this.user.username}).subscribe({
      next: data => {
        console.log('received items', data);
        this.user = data;
        this.groupId = this.user.currGroup.id;
        console.log(this.groupId);
      },
      error: error => {
        console.error(error.message);
      }
    });
  }

  addUser() {
    if(this.userToAdd === undefined || this.userToAdd === null){
      this.showError('Username cannot be null or empty');
      return;
    }
    if(this.userToAdd.length > 100){
      this.showError('Username must be < 100 characters');
      return;
    }
    this.groupService.addUser(this.userToAdd, this.groupId).subscribe({
      next: data => {
        console.log('added user {} to group {}', this.userToAdd, this.groupId);
      },
      error: error => {
        console.error(error.message);
        this.showError('Error while adding user to group: ' + error.error.message);
      }
    });
  }
  public vanishError(): void {
    console.log('vanishError');
    this.error = null;
  }

  private showError(msg: string) {
    console.log('show error' + msg);
    this.error = msg;
  }
}
