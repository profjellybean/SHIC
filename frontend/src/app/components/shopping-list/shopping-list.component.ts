import {Component, OnInit, TemplateRef} from '@angular/core';
import {ShoppingListService} from '../../services/shopping-list.service';
import {Item} from '../../dtos/item';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ShoppingList} from '../../dtos/shopping-list';
import {UnitOfQuantity} from '../../dtos/unitOfQuantity';
import {StorageService} from '../../services/storage.service';
import {ItemService} from '../../services/item.service';
import {Bill} from '../../dtos/bill';
import {User} from '../../dtos/user';
import {GroupService} from '../../services/group.service';
import {UserService} from '../../services/user.service';
import jwt_decode from 'jwt-decode';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-shopping-list',
  templateUrl: './shopping-list.component.html',
  styleUrls: ['./shopping-list.component.scss']
})
export class ShoppingListComponent implements OnInit {

  error = false;
  errorMessage = '';
  submitted = false;

  billToAdd: Bill = {
    id: null,
    registerId: null,
    groceries: null,
    notes: null,
    names: null,
    notPaidNames: null,
    sum: null,
    sumPerPerson: null,
    date: new Date(),
    nameList: null,
    notPaidNameList: null
  };
  itemsAdd: Item[] = null;
  itemToAdd: Item = null;
  itemsToBuy: Item[] = [];
  items: Item[] = null;
  privateList: ShoppingList;
  publicList: ShoppingList;
  isInPublic: boolean;
  groupStorageId: number;
  groupShoppingListId: number;
  unitsOfQuantity: UnitOfQuantity[];
  allUsers: User[] = null;

  user: User = {
    // @ts-ignore
    username: jwt_decode(this.authService.getToken()).sub.trim(),
    id: null,
    currGroup: null,
    privList: null,
    email: null
  };
  groupId: number;
  constructor(private shoppingListService: ShoppingListService,
              private storageService: StorageService,
              private itemService: ItemService,
              private modalService: NgbModal,
              private groupService: GroupService,
              private userService: UserService,
              private authService: AuthService) {
  }

  ngOnInit(): void {
    this.getCurrentGroup();
    this.isInPublic = true;
    this.loadUnitsOfQuantity();
    this.loadItemsToAdd();
    this.loadGroupStorageId();
    this.loadGroupShoppingListId();
    this.getPrivateShoppingList();
    this.getPublicShoppingList();
    console.log('DATE: ' + this.billToAdd.date);

  }

  getCurrentGroup(){
    this.userService.getCurrentUser({username: this.user.username}).subscribe({
      next: data => {
        console.log('received items11', data);
        this.user = data;
        this.groupId = this.user.currGroup.id;
        this.getAllUsers(this.groupId);
        console.log(this.groupId);
      },
      error: error => {
        console.error(error.message);
      }
    });
  }

  getAllUsers(id: number){
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

  switchMode(publicMode: boolean){
    this.isInPublic = publicMode;
    if(publicMode){
      this.items = this.publicList.items;
    }else{
      this.items = this.privateList.items;
    }
  }
  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }


  deleteItem(item: Item){
    if(this.isInPublic){
      this.shoppingListService.deleteItemFromPublic(item.id).subscribe(
        {
          next: res => {
            console.log(res);
            this.removeItemFromShoppingList(item);
          },
          error: err => {
            console.error(err);
            this.defaultServiceErrorHandling(err);
          }
        }
      );
    }else{
      this.shoppingListService.deleteItemFromPrivate(item.id).subscribe(
        {
          next: res => {
            console.log(res);
            this.removeItemFromShoppingList(item);
          },
          error: err => {
            console.error(err);
            this.defaultServiceErrorHandling(err);
          }
        }
      );
    }
  }


  getPrivateShoppingList() {
    this.shoppingListService.getPrivateShoppingList().subscribe({
        next: res => {
          this.privateList = res;
          if(!this.isInPublic){
            this.items = this.privateList.items;
            console.log(' items = private');
          }
          console.log(this.privateList);
        },
        error: err => {
          console.error(err);
          this.defaultServiceErrorHandling(err);
        }
      }
    );
  }

  getPublicShoppingList() {
    console.log('getting public shoppingList');
    this.shoppingListService.getPublicShoppingList().subscribe({
        next: res => {
          this.publicList = res;
          if(this.isInPublic){
            this.items = this.publicList.items;
            console.log(' items = public');
          }
          console.log(this.publicList);
        },
        error: err => {
          console.error(err);
          this.defaultServiceErrorHandling(err);
        }
      }
    );
  }

  workOffShoppingList() {
    this.shoppingListService.workOffShoppingList(this.itemsToBuy, this.groupShoppingListId).subscribe({
        next: res => {
          console.log(res);
          for (const item of this.itemsToBuy) {
            this.removeItemFromShoppingList(item);
          }
        },
        error: err => {
          console.log(err);
          this.defaultServiceErrorHandling(err);
        }
      }
    );
  }

  checkCheckbox(item: Item) {
    this.itemsToBuy.push(item);
  }

  loadUnitsOfQuantity() {
    this.storageService.findAllUnitsOfQuantity().subscribe({
      next: data => {
        console.log('received units of quantity', data);
        this.unitsOfQuantity = data;
      }
    });
  }

  loadItemsToAdd() {
    this.itemService.findAllItemsForGroup().subscribe({
      next: data => {
        console.log('received items to add', data);
        this.itemsAdd = data;
      }
    });
  }

  addItemToShoppingList(item: Item) {
    this.itemToAdd.id = null;
    if(item.quantity === undefined) {
      item.quantity = null;
    }
    if(item.amount === undefined) {
      item.amount = null;
    }
    if(this.isInPublic){
      console.log('add item to public list', item);
      this.shoppingListService.addToPublicShoppingList(item).subscribe({
        next: data => {
          if(data === null){
            this.getPublicShoppingList();
          }else{
            this.items.push(data);
          }

          // todo dont reload every time
          this.loadItemsToAdd();

        },
        error: err => {
          this.defaultServiceErrorHandling(err);
        }
      });
    }else{
      console.log('add item to private list', item);
      this.shoppingListService.addToPrivateShoppingList(item).subscribe({
        next: data => {
          if(data === null){
            this.getPrivateShoppingList();
          }else{
            this.items.push(data);
          }

          // todo dont reload every time
          this.loadItemsToAdd();

        },
        error: err => {
          this.defaultServiceErrorHandling(err);
        }
      });
    }

  }

  addItemForm(form) {
    this.submitted = true;

    if (form.valid) {
      console.log('form item to add', this.itemToAdd);
      this.addItemToShoppingList(this.itemToAdd);
      this.clearForm();
    }
  }

  addBillForm(form) {
    this.submitted = true;

    if (form.valid) {
      console.log('form item to add', this.billToAdd);
      this.addBill(this.billToAdd);
      this.clearForm();
    }
  }

  addBill(bill: Bill){

  }

  openAddModal(itemAddModal: TemplateRef<any>) {
    this.itemToAdd = new Item();
    this.modalService.open(itemAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  openBillModal(billModal: TemplateRef<any>) {
    this.billToAdd = new Bill();
    this.modalService.open(billModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  private removeItemFromShoppingList(item: Item) {
    for (let i = 0; i < this.items.length; i++) {
      if(this.items[i].id === item.id) {
        this.items.splice(i, 1);
      }
    }
  }

  private clearForm() {
    this.itemToAdd = new Item();
    this.submitted = false;
  }

  private loadGroupStorageId() {
    this.shoppingListService.getGroupStorageForUser().subscribe({
      next: data => {
        this.groupStorageId = data;
      },
      error: err => {
        this.defaultServiceErrorHandling(err);
      }
    });
  }

  private loadGroupShoppingListId() {
    this.shoppingListService.getGroupShoppingListForUser().subscribe({
      next: data => {
        this.groupShoppingListId = data;
      },
      error: err => {
        this.defaultServiceErrorHandling(err);
      }
    });
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {

      this.errorMessage = error.error.message;
    } else {
      this.errorMessage = error.error;
    }
  }

}
