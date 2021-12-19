import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {UserService} from '../../services/user.service';
import {AuthRequest} from '../../dtos/auth-request';
import {Username} from '../../dtos/username';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  // After first submission attempt, form validation will start
  submitted = false;
  // Error flag
  error = false;
  errorMessage = '';
  confirmEmailButton = false;

  resendConfirmationUsernameDto: Username = {username:null};

  constructor(private formBuilder: FormBuilder, private authService: AuthService,
              private userService: UserService, private router: Router) {

    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
  loginUser() {
    this.submitted = true;
    if (this.loginForm.valid) {
      const authRequest: AuthRequest = new AuthRequest(this.loginForm.controls.username.value, this.loginForm.controls.password.value);
      this.authenticateUser(authRequest);
    } else {
      console.log('Invalid input');
    }
  }
  resendEmailConfirmation(){
    this.confirmEmailButton = false;
    this.userService.resendConfirmation(this.resendConfirmationUsernameDto).subscribe(
      () => {
        this.confirmEmailButton = false;
      },
      error => {
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.message;
        } else {
          this.errorMessage = error.error;
        }
      }
    );

  }
  /**
   * Send authentication data to the authService. If the authentication was successfully, the user will be forwarded to the message page
   *
   * @param authRequest authentication data from the user login form
   */
  authenticateUser(authRequest: AuthRequest) {
    console.log('Try to authenticate user: ' + authRequest.username);
    this.authService.loginUser(authRequest).subscribe(
      () => {
        console.log('Successfully logged in user: ' + authRequest.username);
        this.router.navigate(['/message']);
      },
      error => {
        console.log('Could not log in due to:');
        console.log(error.error);
        if(error.error === 'Email confirmation needed'){
          this.confirmEmailButton = true;
          this.resendConfirmationUsernameDto.username = authRequest.username;
        }
        this.error = true;
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
      }
    );
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  ngOnInit() {
  }

}
