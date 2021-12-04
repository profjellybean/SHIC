import { Component, OnInit } from '@angular/core';
import {RegisterService} from '../../services/register.service';
import {ActivatedRoute} from '@angular/router';
import {Bill} from '../../dtos/bill';
import {User} from '../../dtos/user';
import {BillService} from '../../services/bill.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  id: number;
  billArray: Bill[];
  monthlyPayment: number;
  monthlyBudget: number;

  bill: Bill = {
    id: null,
    groceries: null,
    notes: null,
    names: null,
    notPaidNames: null,
    sum: null,
    sumPerPerson: null,
    date: null
  };

  user: User = {
    id: null,
    username: null,
    password: null
  };

  constructor(private registerService: RegisterService, private billService: BillService, public route: ActivatedRoute) { }

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.loadAllBills();
  }

  public confirmPayment(id: number) {
    this.loadBill(id);
    this.billService.deleteName(this.bill.id, this.user.id).subscribe(
      (bill: Bill) => {

      },
    );
    this.removeName(this.bill);
  }

  private removeName(bill: Bill) {
    for (let i = 0; i < this.bill.notPaidNames.length; i++) {
      if(this.bill.notPaidNames[i].id === this.user.id) {
        this.bill.notPaidNames.splice(i, 1);
      }
    }
  }

  private loadBill(id: number) {
    this.billService.getBillById(id).subscribe(
      (bill: Bill) => {
        this.bill = bill;
      }
    );
  }

  private loadAllBills() {
    this.billService.getAllBills().subscribe(
      (billArray: Bill[]) => {
        this.billArray = billArray;
      },
    );
  }

}
