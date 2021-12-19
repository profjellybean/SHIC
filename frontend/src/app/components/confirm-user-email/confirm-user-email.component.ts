import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-confirm-user-email',
  templateUrl: './confirm-user-email.component.html',
  styleUrls: ['./confirm-user-email.component.scss']
})
export class ConfirmUserEmailComponent implements OnInit {

  confirmationToken: string;
  confirmationSuccess = false;
  errorMessage: string;
  constructor(private route: ActivatedRoute, private userService: UserService) { }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {


        this.confirmationToken = params.token;
        if(this.confirmationToken){

          this.userService.confirmUser(this.confirmationToken).subscribe(
            () => {

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
