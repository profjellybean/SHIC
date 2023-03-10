import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
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
    this.registerForm = new FormGroup(
      {
        username: new FormControl('', [Validators.required]),
        email: new FormControl('', [Validators.required]),
        password: new FormControl('', [Validators.required, Validators.minLength(8)]),
        confirmPassword: new FormControl('', [Validators.required])
      }, RegisterUserComponent.mustMatch('password', 'confirmPassword')
    );
  }

  static mustMatch(controlName: string, matchingControlName: string) {
    return (formGroup: FormGroup) => {
      const control = formGroup.controls[controlName];
      const matchingControl = formGroup.controls[matchingControlName];

      if (matchingControl.errors && !matchingControl.errors.mustMatch) {
        return;
      }

      // set error on matchingControl if validation fails
      if (control.value !== matchingControl.value) {
        matchingControl.setErrors({mustMatch: true});
      } else {
        matchingControl.setErrors(null);
      }
      return null;
    };
  }

  ngOnInit(): void {
  }


  registerUser() {
    this.submitted = true;
    if (this.registerForm.valid) {
      const registerRequest: RegisterRequest = new RegisterRequest(this.registerForm.controls.username.value,
        this.registerForm.controls.password.value,
        this.registerForm.controls.email.value);
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
        setTimeout(() => this.router.navigate(['/login']), 1000);
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

}
