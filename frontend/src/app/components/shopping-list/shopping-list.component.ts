import {Component, OnInit, TemplateRef} from '@angular/core';
import {ShoppingListService} from '../../services/shopping-list.service';
import {Item} from '../../dtos/item';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ShoppingList} from '../../dtos/shopping-list';
import {UnitOfQuantity} from '../../dtos/unitOfQuantity';
import {StorageService} from '../../services/storage.service';
import {ItemService} from '../../services/item.service';
import {User} from '../../dtos/user';
import {GroupService} from '../../services/group.service';
import {UserService} from '../../services/user.service';
import jwt_decode from 'jwt-decode';
import {AuthService} from '../../services/auth.service';
import {BillService} from '../../services/bill.service';
import {BillDto} from '../../dtos/billDto';
import {NotificationsComponent} from '../notifications/notifications.component';

@Component({
  selector: 'app-shopping-list',
  templateUrl: './shopping-list.component.html',
  styleUrls: ['./shopping-list.component.scss']
})
export class ShoppingListComponent implements OnInit {

  error = false;
  errorMessage = '';
  submitted = false;
  nullUser: User = {
    id: null,
    email: null,
    currGroup: null,
    username: null,
    privList: null,
    image: null
  };

  billToAdd: BillDto = new BillDto();
  itemsAdd: Item[] = null;
  itemToAdd: Item = null;
  itemAmountChange: Item = {
    image: null, id: null, storageId: null, name: null,
    notes: null, expDate: null, amount: 0, locationTag: null, shoppingListId: null, quantity: null
  };
  itemsToBuy: Item[] = [];
  items: Item[] = null;
  privateList: ShoppingList;
  publicList: ShoppingList;
  isInPublic: boolean;
  groupStorageId: number;
  groupShoppingListId: number;
  unitsOfQuantity: UnitOfQuantity[];
  allUsers: User[] = null;
  itemToDelete: Item;

  checkboxValues: boolean[] = [];
  toggleAllItems: boolean;

  user: User = {
    // @ts-ignore
    username: jwt_decode(this.authService.getToken()).sub.trim(),
    id: null,
    currGroup: null,
    privList: null,
    email: null,
    image: null
  };
  groupId: number;
  searchItemByName = null;

  searchString = '';
  searchItem: Item = {
    image: null, id: null, storageId: null, name: null,
    notes: null, expDate: null, amount: 0, locationTag: null, shoppingListId: null, quantity: null
  };

  constructor(private shoppingListService: ShoppingListService,
              private storageService: StorageService,
              private itemService: ItemService,
              private modalService: NgbModal,
              private groupService: GroupService,
              private userService: UserService,
              private authService: AuthService,
              private billService: BillService,
              private notifications: NotificationsComponent
  ) {
  }

  ngOnInit(): void {
    this.getCurrentGroup();
    this.isInPublic = true;
    this.loadUnitsOfQuantity();
    this.loadGroupStorageId();
    this.loadGroupShoppingListId();
    this.getPrivateShoppingList();
    this.getPublicShoppingList();
    this.toggleAllItems = false;

  }

  getCurrentGroup() {
    this.userService.getCurrentUser({username: this.user.username}).subscribe({
      next: data => {
        console.log('received items11', data);
        this.user = data;
        this.groupId = this.user.currGroup.id;
        this.getAllUsers(this.groupId);
        if (this.searchItemByName == null) {
          this.loadItemsToAdd();
        } else {
          this.searchItemsToAdd();
        }
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


  switchMode(publicMode: boolean) {
    this.isInPublic = publicMode;
    if (publicMode) {
      this.items = this.publicList.items;
      this.searchItem.shoppingListId = this.publicList.id;
    } else {
      this.items = this.privateList.items;
      this.searchItem.shoppingListId = this.privateList.id;
    }
  }

  searchItems() {
    this.searchString = this.createSearchString();
    this.shoppingListService.searchItems(this.searchString).subscribe({
      next: data => {
        console.log('found data', data);
        this.items = data;
      },
      error: error => {
        console.error(error.message);
        this.defaultServiceErrorHandling(error);
      }
    });
  }


  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }


  deleteItem(item: Item) {
    if (this.isInPublic) {
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
    } else {
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
          if (!this.isInPublic) {
            this.items = this.privateList.items;
            console.log('items = private');
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
          if (this.isInPublic) {
            this.items = this.publicList.items;
            console.log('items = public');
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
            this.toggleAllItems = false;
          }
        },
        error: err => {
          console.log(err);
          this.defaultServiceErrorHandling(err);
        }
      }
    );
  }

  toggleAll() {
    if (this.toggleAllItems === true) {
      console.log('select all: ' + this.toggleAllItems);
      for (let i = 0; i < this.items.length; i++) {
        this.decheckCheckbox(this.items[i]);
        this.checkboxValues[i] = false;
      }
    } else {
      for (let i = 0; i < this.items.length; i++) {
        this.checkCheckbox(this.items[i]);
        this.checkboxValues[i] = true;
      }
    }
  }

  toggle(item: Item, i: number) {
    if (this.checkboxValues[i] === true) {
      this.decheckCheckbox(item);
    } else {
      this.checkCheckbox(item);
    }
  }

  checkCheckbox(item: Item) {
    this.itemsToBuy.push(item);
  }

  decheckCheckbox(item: Item) {
    for (let i = 0; i < this.itemsToBuy.length; i++) {
      if (this.itemsToBuy[i].name === item.name) {
        this.itemsToBuy.splice(i, 1);
      }
    }
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
        if (this.itemsAdd.length > 5) {
          this.itemsAdd = this.itemsAdd.splice(0, 5);
        }
      },
      error: error => {
        this.defaultServiceErrorHandling(error);
      }
    });
  }

  searchItemsToAdd() {
    this.itemService.searchItemsByName(this.searchItemByName).subscribe({
      next: data => {
        console.log('received items to add by ' + this.searchItemByName, data);
        this.itemsAdd = data;
        if(this.itemsAdd.length > 5) {
          this.itemsAdd = this.itemsAdd.splice(0,5);
        }
      }
    });
  }

  addItemToShoppingList(item: Item) {
    this.itemToAdd.id = null;
    if (item.quantity === undefined) {
      item.quantity = null;
    }
    if (item.amount === undefined) {
      item.amount = null;
    }
    if (this.isInPublic) {
      console.log('add item to public list', item);
      this.shoppingListService.addToPublicShoppingList(item).subscribe({
        next: data => {
          if (data === null) {
            this.getPublicShoppingList();
          } else {
            this.items.push(data);
            this.checkboxValues.push(false);
          }

          // todo dont reload every time
          this.loadItemsToAdd();

        },
        error: err => {
          this.defaultServiceErrorHandling(err);
        }
      });
    } else {
      console.log('add item to private list', item);
      this.shoppingListService.addToPrivateShoppingList(item).subscribe({
        next: data => {
          if (data === null) {
            this.getPrivateShoppingList();
          } else {
            this.items.push(data);
            this.checkboxValues.push(false);
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

  changeAmountOfItemInShoppingList(item: Item) {
    this.setItemAmountChange(item);
    console.log('item amount change ', this.itemAmountChange);
    this.removeItemFromShoppingList(item);
    console.log('deleted old item ', item);

    if (this.isInPublic) {
      console.log('change amount if item in public list', item);
      this.shoppingListService.changeAmountOfItemInPublicShoppingList(this.itemAmountChange).subscribe({
        next: data => {
          if (data === null) {
            this.getPublicShoppingList();
          } else {
            this.items.push(data);
            this.checkboxValues.push(false);
          }
          // todo dont reload every time
          this.loadItemsToAdd();
        },
        error: err => {
          this.defaultServiceErrorHandling(err);
        }
      });
    } else {
      console.log('change amount of item of private list', item);
      this.shoppingListService.changeAmountOfItemInPrivateShoppingList(this.itemAmountChange).subscribe({
        next: data => {
          console.log('hey i am back ', data);
          if (data === null) {
            this.getPrivateShoppingList();
          } else {
            this.items.push(data);
            this.checkboxValues.push(false);
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

  isThisNumber(number: any): boolean {
    return !isNaN(parseFloat(number)) && !isNaN(number - 0);
  }

  isValidDate(date: Date): boolean {
    if (this.submitted) {
      console.log('Validate date');
      if (date === null) {
        return false;
      }
      const datee = new Date();
      datee.setFullYear(Number(date.toString().substring(0, 4)), Number(date.toString().substring(6, 7)),
        Number(date.toString().substring(9, 10)));
      const now = new Date();
      console.log(datee <= now);
      return datee <= now;
    }
  }


  addBillForm(form) {
    console.log('Add bill');
    this.submitted = true;
    if (form.valid && this.itemsToBuy.length !== 0) {
      console.log('form item to add', this.billToAdd.names);

      if (this.billToAdd.names[0].id === null) {
        console.log('Set allUsers {}', this.allUsers);
        this.billToAdd.names = this.allUsers;
      }
      for (const item of this.itemsToBuy) {
        item.storageId = this.groupStorageId;
        this.billToAdd.groceries.push(item);
      }
      this.billToAdd.notPaidNames = this.billToAdd.names;
      if (this.billToAdd.names.length > 0) {
        this.billToAdd.sumPerPerson = this.billToAdd.sum / this.billToAdd.names.length;
      } else {
        this.billToAdd.sumPerPerson = this.billToAdd.sum;
      }
      for (const name of this.billToAdd.names) {
        delete name.currGroup;
      }
      for (const name of this.billToAdd.notPaidNames) {
        delete name.currGroup;
      }
      this.billToAdd.registerId = this.user.currGroup.registerId;
      this.addBill(this.billToAdd);
      this.workOffShoppingList();
      this.clearForm();
      this.modalService.dismissAll();
    }
  }

  addBill(bill: BillDto) {
    console.log(bill);
    this.billService.bill(bill).subscribe({
      next: data => {
        console.log(data);
      }
      ,
      error: err => {
        this.defaultServiceErrorHandling(err);
      }
    });
  }

  openAddModal(itemAddModal: TemplateRef<any>) {
    this.itemToAdd = new Item();
    this.modalService.open(itemAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  openBillModal(billModal: TemplateRef<any>) {
    this.billToAdd = new BillDto();
    this.modalService.open(billModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  private createSearchString(): string {
    this.searchString = '?id=';
    if (this.searchItem.shoppingListId != null) {
      this.searchString = this.searchString + '&shoppingListId=' + this.searchItem.shoppingListId;
    } else {
      this.searchString = this.searchString + '&shoppingListId=';
    }
    if (this.searchItem.shoppingListId != null) {
      if(this.isInPublic) {
        this.searchString = this.searchString + '&shoppingListId=' + this.publicList.id;
      } else {
        this.searchString = this.searchString + '&shoppingListId=' + this.privateList.id;
      }
    } else {
      this.searchString = this.searchString + '&shoppingListId=';
    }
    if (this.searchItem.name != null) {
      if (this.searchItem.name.trim() !== '') {
        this.searchString = this.searchString + '&name=' + this.searchItem.name;
      } else {
        this.searchString = this.searchString + '&name=';
      }
    } else {
      this.searchString = this.searchString + '&name=';
    }
    if (this.searchItem.notes != null) {
      if (this.searchItem.notes.trim() !== '') {
        this.searchString = this.searchString + '&notes=' + this.searchItem.notes;
      } else {
        this.searchString = this.searchString + '&notes=';
      }
    } else {
      this.searchString = this.searchString + '&notes=';
    }
    if (this.searchItem.amount != null) {
      this.searchString = this.searchString + '&amount=' + this.searchItem.amount;
    } else {
      this.searchString = this.searchString + '&amount=0';
    }
    if (this.searchItem.locationTag != null) {
      this.searchString = this.searchString + '&locationTag=' + this.searchItem.locationTag;
    } else {
      this.searchString = this.searchString + '&locationTag=';
    }
    this.searchString = this.searchString + '&quantity=&image=';
    if (this.searchItem.expDate != null) {
      this.searchString = this.searchString + '&expDate=' + this.searchItem.expDate;
    } else {
      this.searchString = this.searchString + '&expDate=';
    }


    return this.searchString;
  }

  private removeItemFromShoppingList(item: Item) {
    for (let i = 0; i < this.items.length; i++) {
      if (this.items[i].id === item.id) {
        this.items.splice(i, 1);
        this.checkboxValues.splice(i, 1);
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
        this.searchItem.shoppingListId = data;
      },
      error: err => {
        this.defaultServiceErrorHandling(err);
      }
    });
  }

  private setItemAmountChange(item: Item) {
    this.itemAmountChange.id = item.id;
    if (item.quantity === undefined) {
      this.itemAmountChange.quantity = null;
    } else {
      this.itemAmountChange.quantity = item.quantity;
    }
    if (item.amount === undefined) {
      this.itemAmountChange.amount = null;
    } else {
      this.itemAmountChange.amount = item.amount;
    }
    if (item.name === undefined) {
      this.itemAmountChange.name = null;
    } else {
      this.itemAmountChange.name = item.name;
    }
    if (item.notes === undefined) {
      this.itemAmountChange.notes = null;
    } else {
      this.itemAmountChange.notes = item.notes;
    }
    if (item.image === undefined) {
      this.itemAmountChange.image = null;
    } else {
      this.itemAmountChange.image = item.image;
    }
    if (item.expDate === undefined) {
      this.itemAmountChange.expDate = null;
    } else {
      this.itemAmountChange.expDate = item.expDate;
    }
    if (item.locationTag === undefined) {
      this.itemAmountChange.locationTag = null;
    } else {
      this.itemAmountChange.locationTag = item.locationTag;
    }
    if (item.storageId === undefined) {
      this.itemAmountChange.storageId = null;
    } else {
      this.itemAmountChange.storageId = item.storageId;
    }
    if (item.shoppingListId === undefined) {
      this.itemAmountChange.shoppingListId = null;
    } else {
      this.itemAmountChange.shoppingListId = item.shoppingListId;
    }
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
