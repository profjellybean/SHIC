import {Component, OnInit, TemplateRef} from '@angular/core';
import {GroupService} from '../../services/group.service';
import {AuthService} from '../../services/auth.service';
import {UserService} from '../../services/user.service';
import {NotificationsComponent} from '../notifications/notifications.component';
import jwt_decode from 'jwt-decode';
import {User} from '../../dtos/user';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {HeaderComponent} from '../header/header.component';


@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {
  groupId = null;
  userToAdd: string;
  error: string;
  success: string;
  users: User[];
  userEditMode: boolean;

  user: User = {
    // @ts-ignore
    username: jwt_decode(this.authService.getToken()).sub.trim(),
    id: null,
    currGroup: null,
    privList: null,
    email: null,
    image: null
  };

  editedUser: User ={
    username: this.user.username,
    email: null,
    id: null,
    currGroup: null,
    privList: null,
    image: null
  };

  constructor(private groupService: GroupService,
              public authService: AuthService,
              private userService: UserService,
              private modalService: NgbModal,
              private notifications: NotificationsComponent) { }

  ngOnInit(): void {
    this.getCurrentGroup();
  }

  generateGroup(){
    this.groupService.generateGroup('WG-Wipplingerstraße', this.user.username).subscribe({
      next: data => {
        console.log('received items10', data);
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
        console.log('received items11', data);
        this.user = data;
        this.groupId = this.user.currGroup.id;
        this.getAllUsers(this.groupId);
        console.log(this.groupId);
      },
      error: error => {
        console.error(error.message);
      }
    });
  }

  getAllUsers(id: number){
    this.groupService.getAllUsers(id).subscribe({
      next: data => {
        console.log('received items', data);
        this.users = data;
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
        this.notifications.pushSuccess('User added successfully');
      },
      error: error => {
        console.error(error.message);
        this.showError('Error while adding user to group: ' + error.error.message);
      }
    });
  }

  onFileChange(event){
    this.editedUser.image = event.target.files[0];
    console.log(event);
    console.log(this.editedUser);

    this.userService.editPicture(this.editedUser.image).subscribe({
      next: data => {
        this.user.image = data.image;
        this.notifications.pushSuccess('New profile picture set!');
      },
      error: error => {
        console.error(error.message);
        this.notifications.pushFailure(error.message);
      }
    });
  }

  editUsername(){
    this.userEditMode = false;
    if(this.editedUser.username !== this.user.username){
      this.userService.editUsername(this.editedUser.username).subscribe({
        next: data => {

          console.log(data);
          // @ts-ignore
          this.authService.setToken(data.token);
          HeaderComponent.username = this.editedUser.username;
          for (const user of this.users) {
            if(user.username === this.user.username ){
              user.username = this.editedUser.username;
            }
          }

          this.user.username = this.editedUser.username;
          this.notifications.pushSuccess('Username changed successfully');
        },
        error: error => {
          console.log(error);
          this.notifications.pushFailure(error.error.message);
          this.editedUser.username = this.user.username;
        }
      });

    }

  }

  deleteUserById() {
    const currentId = this.user.id;
    this.authService.logoutUser();

    this.userService.deleteUserById(currentId).subscribe({
      next: data => {

      },
      error: error => {
        console.error(error.message);
        this.showError('Error while deleting user: ' + error.error.message);
      }
    });
  }

  openAddModal(userDeleteModal: TemplateRef<any>) {
    this.modalService.open(userDeleteModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  public vanishError(): void {
    this.error = null;
  }

  public vanishSuccess(): void {
    this.success = null;
  }

  private showError(msg: string) {
    this.error = msg;
  }

  private showSuccess(msg: string) {
    this.success = msg;
  }
}
