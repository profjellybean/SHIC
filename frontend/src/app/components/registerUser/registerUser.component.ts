import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {UserService} from '../../services/user.service';
import {RegisterRequest} from '../../dtos/RegisterRequest';


@Component({
  selector: 'app-login',
  templateUrl: './registerUser.component.html',
  styleUrls: ['./registerUser.component.scss']
})
export class RegisterUserComponent implements OnInit {

  registerForm: FormGroup;
  // After first submission attempt, form validation will start
  submitted = false;
  // Error flag
  error = false;
  errorMessage = '';
  success = false;
  successMessage = '';

  constructor(private formBuilder: FormBuilder, private userService: UserService, private router: Router) {
    this.registerForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
  registerUser() {
    this.submitted = true;
    if (this.registerForm.valid) {
      const registerRequest: RegisterRequest = new RegisterRequest(this.registerForm.controls.username.value,
                                                                   this.registerForm.controls.password.value);
      this.sendUserRegistration(registerRequest);
    } else {
      console.log('Invalid input');
    }
  }

  /**
   * Send authentication data to the authService. If the authentication was successfully, the user will be forwarded to the message page
   *
   * @param registerRequest Dto for user registration
   */
  sendUserRegistration(registerRequest: RegisterRequest) {
    console.log('Try to register user: ' + registerRequest.username);
    this.userService.registerUser(registerRequest).subscribe(
      () => {
        console.log('Successfully registered user: ' + registerRequest.username);
        this.success = true;
        this.successMessage = 'User registrated successfully! Redirect to login...';
        setTimeout(()=> this.router.navigate(['/login']),900);
      },
      error => {
        console.log('Could not register due to:');
        console.log(error);
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
