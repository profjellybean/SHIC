import {Component, OnInit, TemplateRef} from '@angular/core';
import {RegisterService} from '../../services/register.service';
import {ActivatedRoute} from '@angular/router';
import {Bill} from '../../dtos/bill';
import {User} from '../../dtos/user';
import {BillService} from '../../services/bill.service';
import {Register} from '../../dtos/register';
// @ts-ignore
import jwt_decode from 'jwt-decode';
import {AuthService} from '../../services/auth.service';
import {UserService} from '../../services/user.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {BillDto} from '../../dtos/billDto';
import {GroupService} from '../../services/group.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  error = false;
  errorMessage = '';
  submitted = false;

  nullUser: User = {
    id: null,
    email: null,
    currGroup: null,
    username: null,
    privList: null
  };

  register: Register = {
    id: null,
    bills: null,
    monthlyPayments: null,
    monthlyBudget: null,
  };
  billToEdit: BillDto;
  allUsers: User[] = null;

  counter = 1;
  secondCounter = 1;
  billArray: Bill[] = [];
  help: string;
  billId: number;
  monthlySum: number;
  newMonthlyBudget: number;
  monthlyDifference: number;
  billSumGroup: number;
  billSumUser: number;

  user: User = {
    // @ts-ignore
    username: jwt_decode(this.authService.getToken()).sub.trim(),
    id: null,
    currGroup: null,
    privList: null,
    email: null
  };

  constructor(private registerService: RegisterService, private billService: BillService, public route: ActivatedRoute,
              private authService: AuthService, private userService: UserService,
              private modalService: NgbModal, private groupService: GroupService) {
  }

  ngOnInit(): void {
    this.getCurrentGroup();
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  getCurrentGroup() {
    this.userService.getCurrentUser({username: this.user.username}).subscribe({
      next: data => {
        console.log('received items11', data);
        this.user = data;
        this.getAllUsers(data.currGroup.id);
        this.loadRegister(data.currGroup.registerId);
        this.getMonthlySum();
        this.getBillSumGroup();
        this.getBillSumUser();
      },
      error: error => {
        console.error(error.message);
      }
    });
  }

  getAllUsers(id: number) {
    this.groupService.getAllUsers(id).subscribe({
      next: data => {
        console.log('received items', data);
        this.allUsers = data;
      },
      error: error => {
        console.error(error.message);
      }
    });
  }

  editBudgetForm(form) {
    this.submitted = true;

    if (form.valid) {
      console.log('form edit monthly budget', this.newMonthlyBudget);
      this.editMonthlyBudget(this.newMonthlyBudget);
      //this.clearForm();
    }
  }

  openEditBudgetModal(editBudgetModal: TemplateRef<any>) {
    this.modalService.open(editBudgetModal, {ariaLabelledBy: 'modal-basic-title'});
  }


  public confirmPayment(billId: number) {

    this.registerService.confirmPayment(billId, this.user.username, 1).subscribe({
      next: register => {
        this.register.id = register.id;
        this.register.bills = register.bills;
        this.register.monthlyPayments = register.monthlyPayments;
        this.register.monthlyBudget = register.monthlyBudget;
        this.loadRegister(this.user.currGroup.registerId);
        this.getBillSumUser();
      }, error: err => {
        this.defaultServiceErrorHandling(err);
      }
    });
  }

  loadRegister(id: number) {
    this.registerService.getRegisterById(id).subscribe({
      next: register => {
        this.register.id = register.id;
        this.register.bills = register.bills;
        this.register.monthlyPayments = register.monthlyPayments;
        this.register.monthlyBudget = register.monthlyBudget;
        this.monthlyDifference = this.register.monthlyBudget - this.monthlySum;

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
          }
          this.billArray[this.counter] = bill;
          this.counter++;
        }
        console.log(this.billArray);
        this.newMonthlyBudget = register.monthlyBudget;
      }, error: err => {
        this.defaultServiceErrorHandling(err);
      }
    });
  }

  openBillModal(billModal: TemplateRef<any>, bill: Bill) {
    this.billToEdit = new BillDto();
    this.billToEdit.date = bill.date;
    this.billToEdit.registerId = bill.registerId;
    this.billToEdit.id = bill.id;
    bill.names.forEach(e => this.billToEdit.names.push(new User(e.id, e.username, e.currGroup, e.privList, e.email)));
    bill.notPaidNames.forEach(e => this.billToEdit.notPaidNames.push(new User(e.id, e.username, e.currGroup, e.privList, e.email)));
    this.billToEdit.groceries = Array.from(bill.groceries.values());
    this.billToEdit.sumPerPerson = bill.sumPerPerson;
    this.billToEdit.sum = bill.sum;
    this.billToEdit.notes = bill.notes;
    this.modalService.open(billModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  editBill(form) {
    this.submitted = true;

    if (form.valid) {
      console.log('form item to add', this.billToEdit);
      if(this.billToEdit.names[0].id === null){
        console.log('Set allUsers {}', this.allUsers);
        this.billToEdit.names = this.allUsers;
      }
      this.billToEdit.notPaidNames = this.billToEdit.names;
      if(this.billToEdit.names.length > 0) {
        this.billToEdit.sumPerPerson = this.billToEdit.sum / this.billToEdit.names.length;
      } else {
        this.billToEdit.sumPerPerson = this.billToEdit.sum;
      }
      for(const name of this.billToEdit.names){
        delete name.currGroup;
      }
      for(const name of this.billToEdit.notPaidNames){
        delete name.currGroup;
      }
      this.billService.editBill(this.billToEdit).subscribe({
        next: data => {
          console.log('received sum of all Bills this month', data);
          this.loadRegister(this.user.currGroup.registerId);
        },
        error: error => {
          console.error(error.message);
          this.defaultServiceErrorHandling(error);
        }
      });
      this.clearForm();
    }
  }

  private getMonthlySum() {
    console.log('getting sum of all Bills this month');
    this.registerService.getMonthlySum().subscribe({
      next: data => {
        console.log('received sum of all Bills this month', data);
        this.monthlySum = data;
      },
      error: error => {
        console.error(error.message);
        this.defaultServiceErrorHandling(error);
      }
    });
  }

  private getBillSumGroup() {
    console.log('getting sum of all Bills for the group');
    this.registerService.getBillSumGroup().subscribe({
      next: data => {
        console.log('received um of all Bills for the group', data);
        this.billSumGroup = data;
      },
      error: error => {
        console.error(error.message);
        this.defaultServiceErrorHandling(error);
      }
    });
  }

  private getBillSumUser() {
    console.log('getting sum of all Bills for the user');
    this.registerService.getBillSumUser().subscribe({
      next: data => {
        console.log('received um of all Bills for the user', data);
        this.billSumUser = data;
      },
      error: error => {
        console.error(error.message);
        this.defaultServiceErrorHandling(error);
      }
    });
  }

  private editMonthlyBudget(budget: number) {
    console.log('edit monthly budget', budget);
    this.registerService.editMonthlyBudget(budget).subscribe({
      next: data => {
        console.log('edited monthly budget', data);
        this.register.monthlyBudget = data;
        this.monthlyDifference = this.register.monthlyBudget - this.monthlySum;
      },
      error: error => {
        console.error(error.message);
        this.defaultServiceErrorHandling(error);
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

  private clearForm() {

  }
}
