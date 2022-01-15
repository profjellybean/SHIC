import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../../services/user.service';
import {NotificationsComponent} from '../notifications/notifications.component';

@Component({
  selector: 'app-confirm-user-email-changed',
  templateUrl: './confirm-user-email-changed.component.html',
  styleUrls: ['./confirm-user-email-changed.component.scss']
})
export class ConfirmUserEmailChangedComponent implements OnInit {

  confirmationToken: string;
  confirmationSuccess = false;
  errorMessage: string;

  constructor(private route: ActivatedRoute, private userService: UserService,
              private router: Router, private notifications: NotificationsComponent) { }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {


      this.confirmationToken = params.token;
      if(this.confirmationToken){

        this.userService.confirmUserChanged(this.confirmationToken).subscribe(
          () => {
            this.router.navigateByUrl('/user');
            this.notifications.pushSuccess('New email set!');
            this.confirmationSuccess = true;
          },
          error => {

            this.confirmationSuccess = false;
            if(error.status === 500){
              this.errorMessage = 'Invalid token';
            }else{

              this.errorMessage = error.error.message;

            }

          }
        );
      }else{

        this.confirmationSuccess = false;
        this.errorMessage = 'Invalid token';
      }
    });
  }

}
