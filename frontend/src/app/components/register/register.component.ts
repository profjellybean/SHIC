import { Component, OnInit } from '@angular/core';
import {RegisterService} from '../../services/register.service';
import {ActivatedRoute} from '@angular/router';
import {Bill} from '../../dtos/bill';
import {ApplikationUser} from '../../dtos/applikationUser';
import {BillService} from '../../services/bill.service';
import {Register} from '../../dtos/register';

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
  names: ApplikationUser[] = [];
  notPaidNames: ApplikationUser[] = [];

  help: string;

  bill: Bill = {
    id: null,
    registerId: null,
    groceries: null,
    notes: null,
    names: null,
    notPaidNames: null,
    sum: null,
    sumPerPerson: null,
    date: null,
    nameList: '',
    notPaidNameList: ''
  };

  user: ApplikationUser = {
    id: 1,
    username: 'tom@email.com',
    password: 'password'
  };

  constructor(private registerService: RegisterService, private billService: BillService, public route: ActivatedRoute) { }

  ngOnInit(): void {
    //const id = +this.route.snapshot.paramMap.get('id');
    this.loadRegister(1);
    console.log('names ' + this.bill.nameList);
    console.log('notPaid ' + this.bill.notPaidNameList);
    console.log('names array ' + this.names);
  }

  public confirmPayment(id: number) {
    this.loadBill(id);

    this.registerService.confirmPayment(1, this.user.id, 1).subscribe(
      (register: Register) => {
        this.register.id = register.id;
        this.register.bills = register.bills;
        this.register.monthlyPayments = register.monthlyPayments;
        this.register.monthlyBudget = register.monthlyBudget;
      },
    );
    this.removeName(this.bill);
    this.loadRegister(1);
  }

  private removeName(bill: Bill) {
    for (let i = 0; i < this.notPaidNames.length; i++) {
      if(this.notPaidNames[i].id === this.user.id) {
        this.notPaidNames.splice(i, 1);
      }
    }
  }

  private loadBill(id: number) {
    this.billService.getBillById(id).subscribe(
      (bill: Bill) => {
        this.bill.id = bill.id;
        this.bill.groceries = bill.groceries;
        this.bill.notes = bill.notes;
        this.bill.names = bill.names;
        this.bill.notPaidNames = bill.notPaidNames;
        this.bill.sum = bill.sum;
        this.bill.sumPerPerson = bill.sumPerPerson;
        this.bill.date = bill.date;
        console.log('bill ' + this.bill.nameList);
      }
    );
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
            this.names[this.secondCounter] = name;
            bill.nameList = bill.nameList + name.username + ', ';
            this.secondCounter++;
          }
          this.secondCounter = 0;
          for (const name of bill.notPaidNames) {
            this.notPaidNames[this.secondCounter] = name;
            bill.notPaidNameList = bill.notPaidNameList + name.username + ', ';
            this.secondCounter++;
          }
          this.billArray[this.counter] = bill;
          this.counter++;
        }
        console.log('names ' + this.bill.nameList);
        console.log('notPaid ' + this.bill.notPaidNameList);
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
