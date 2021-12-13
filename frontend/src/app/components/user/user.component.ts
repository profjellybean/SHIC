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
      },
      error: error => {
        console.error(error.message);
      }
    });
  }
}
