import {Component, OnInit} from '@angular/core';
import {RegisterService} from '../../services/register.service';
import {ActivatedRoute} from '@angular/router';
import {Bill} from '../../dtos/bill';
import {User} from '../../dtos/user';
import {BillService} from '../../services/bill.service';
import {Register} from '../../dtos/register';
// @ts-ignore
import jwt_decode from 'jwt-decode';
import {AuthService} from '../../services/auth.service';
import {identifierModuleUrl} from '@angular/compiler';
import {UserService} from "../../services/user.service";

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
  monthlySum: number;

  user: User = {
    // @ts-ignore
    username: jwt_decode(this.authService.getToken()).sub.trim(),
    id: null,
    currGroup: null,
    privList: null,
    email: null
  };

  constructor(private registerService: RegisterService, private billService: BillService, public route: ActivatedRoute,
              private authService: AuthService, private userService: UserService) {
  }

  ngOnInit(): void {
    this.getCurrentGroup();
    this.getMonthlySum();
  }

  getCurrentGroup(){
    this.userService.getCurrentUser({username: this.user.username}).subscribe({
      next: data => {
        console.log('received items11', data);
        this.user = data;
        this.loadRegister(data.currGroup.registerId);
      },
      error: error => {
        console.error(error.message);
      }
    });
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

  private getMonthlySum() {
    this.registerService.getMonthlySum().subscribe({
      next: data => {
        console.log('received sum of all Bills this month', data);
        this.monthlySum = data;
      },
      error: error => {
        console.error(error.message);
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
