import {Component, OnInit} from '@angular/core';
import {RegisterService} from '../../services/register.service';
import {ActivatedRoute} from '@angular/router';
import {Bill} from '../../dtos/bill';
import {User} from '../../dtos/User';
import {BillService} from '../../services/bill.service';
import {Register} from '../../dtos/register';
// @ts-ignore
import jwt_decode from 'jwt-decode';
import {AuthService} from '../../services/auth.service';
import {identifierModuleUrl} from '@angular/compiler';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  error = false;
  errorMessage = '';

  register: Register = {
    id: null,
    bills: null,
    monthlyPayments: null,
    monthlyBudget: null,
  };

  counter = 1;
  secondCounter = 1;
  billArray: Bill[] = [];
  help: string;
  billId: number;

  user: User = {
    // @ts-ignore
    username: jwt_decode(this.authService.getToken()).sub.trim(),
    password: null,
    id: null,
    currGroup: null,
    privList: null
  };

  constructor(private registerService: RegisterService, private billService: BillService, public route: ActivatedRoute,
              private authService: AuthService) {
  }

  ngOnInit(): void {
    //const id = +this.route.snapshot.paramMap.get('id');
    this.loadRegister(1);
  }

  public confirmPayment(billId: number) {

    this.registerService.confirmPayment(billId, this.user.username, 1).subscribe({
      next: register => {
        this.register.id = register.id;
        this.register.bills = register.bills;
        this.register.monthlyPayments = register.monthlyPayments;
        this.register.monthlyBudget = register.monthlyBudget;
        this.loadRegister(1);
      }, error: err => {
        this.defaultServiceErrorHandling(err);
      }
    });
  }

  private loadRegister(id: number) {
    this.registerService.getRegisterById(id).subscribe({
      next: register => {
        this.register.id = register.id;
        this.register.bills = register.bills;
        this.register.monthlyPayments = register.monthlyPayments;
        this.register.monthlyBudget = register.monthlyBudget;

        this.counter = 0;
        for (const bill of register.bills) {
          this.secondCounter = 0;
          bill.nameList = '';
          bill.notPaidNameList = '';
          for (const name of bill.names) {
            bill.nameList = bill.nameList + name.username + ', ';
            this.secondCounter++;
          }
          this.secondCounter = 0;
          for (const name of bill.notPaidNames) {
            bill.notPaidNameList = bill.notPaidNameList + name.username + ', ';
            this.secondCounter++;
            console.log('not paid ' + bill.notPaidNameList);
          }
          this.billArray[this.counter] = bill;
          this.counter++;
        }
      }, error: err => {
        this.defaultServiceErrorHandling(err);
      }
    });
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }
}
