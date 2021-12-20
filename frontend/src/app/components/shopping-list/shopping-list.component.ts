import {Component, OnInit, TemplateRef} from '@angular/core';
import {ShoppingListService} from '../../services/shopping-list.service';
import {Item} from '../../dtos/item';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {User} from '../../dtos/user';
// @ts-ignore
import jwt_decode from 'jwt-decode';
import {AuthService} from '../../services/auth.service';
import {UserService} from '../../services/user.service';
import {ShoppingList} from '../../dtos/shopping-list';
import {GroupService} from '../../services/group.service';

@Component({
  selector: 'app-shopping-list',
  templateUrl: './shopping-list.component.html',
  styleUrls: ['./shopping-list.component.scss']
})
export class ShoppingListComponent implements OnInit {

  error = false;
  errorMessage = '';
  submitted = false;

  itemsAdd: Item[] = null;
  itemToAdd: Item = null;
  itemsToBuy: Item[] = [];
  items: Item[] = null;
  privateList: ShoppingList;
  publicList: ShoppingList;
  isInPublic: boolean;
  groupStorageId: number;
  user: User = {
    // @ts-ignore
    username: jwt_decode(this.authService.getToken()).sub.trim(),
    password: null,
    id: null,
    currGroup: null,
    privList: null
  };


  constructor(private shoppingListService: ShoppingListService,
              private modalService: NgbModal,
              private authService: AuthService,
              private userService: UserService,
              private groupService: GroupService) {
  }

  ngOnInit(): void {
    this.loadItemsToAdd();
    this.loadGroupStorageId();
    this.getPrivateShoppingList();
    this.getPublicShoppingList();
    this.getCurrUser();
  }

  switchMode(publicMode: boolean){
    this.isInPublic = publicMode;
    if(publicMode){
      this.items = this.publicList.items;
    }else{
      this.items = this.privateList.items;
    }

  }

  getCurrUser(){
    this.userService.getCurrentUser({username: this.user.username}).subscribe({
      next: data => {
        console.log('received items1', data);
        this.user = data;
        this.loadItems();
      },
      error: error => {
        console.error(error.message);
      }
    });
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  getPrivateShoppingList() {
    this.shoppingListService.getPrivateShoppingList().subscribe({
        next: res => {
          this.privateList = res;
          console.log(this.privateList);
        },
        error: err => {
          console.error(err);
        }
      }
    );
  }

  getPublicShoppingList() {
    this.shoppingListService.getPublicShoppingList().subscribe({
        next: res => {
          this.publicList = res;
          console.log(this.publicList);
          if(this.items === null){
            this.items = this.publicList.items;
          }
        },
        error: err => {
          console.error(err);
        }
      }
    );
  }

  workOffShoppingList() {
    this.shoppingListService.workOffShoppingList(this.itemsToBuy, this.user.currGroup.publicShoppingListId).subscribe({
        next: res => {
          console.log(res);
          for (const item of this.itemsToBuy) {
            this.removeItemFromShoppingList(item);
          }
        },
        error: err => {
          console.log(err);
        }
      }
    );
  }

  checkCheckbox(item: Item) {
    this.itemsToBuy.push(item);
  }

  loadItemsToAdd() {
    this.shoppingListService.findAllItems().subscribe({
      next: data => {
        console.log('received items2', data);
        this.itemsAdd = data;
      }
    });
  }

  loadItems() {
    if(this.isInPublic){
      this.shoppingListService.findAll(this.user.currGroup.publicShoppingListId).subscribe({
        next: data => {
          console.log('received items3', data);

          this.items = data;
        }
      });
    }else{
      this.shoppingListService.findAll(this.user.privList).subscribe({
        next: data => {
          console.log('received items4', data);

          this.items = data;
        }
      });
    }

  }

  addItemToShoppingList2() {
    console.log('item to add', this.itemToAdd);

    this.itemToAdd.id = null;
    this.itemToAdd.amount = null;
    this.itemToAdd.quantity = null;
    console.log('item to add', this.itemToAdd);
    this.shoppingListService.addItemToShoppingList(this.itemToAdd).subscribe({
      next: data => {
        this.items.push(this.itemToAdd);
        this.loadItems();
        console.log('add item', data);
      },
      error: err => {
        this.defaultServiceErrorHandling(err);
      }
    });
  }

  addItemToShoppingList() {
    console.log('item to add', this.itemToAdd);

    this.itemToAdd.id = null;
    this.itemToAdd.amount = null;
    this.itemToAdd.quantity = null;
    console.log('item to add', this.itemToAdd);
    if(this.isInPublic){
      this.shoppingListService.addToPublicShoppingList(this.itemToAdd).subscribe({
        next: data => {
          this.items.push(data);
          console.log('add item', data);
        },
        error: err => {
          this.defaultServiceErrorHandling(err);
        }
      });
    }else{
      this.shoppingListService.addToPrivateShoppingList(this.itemToAdd).subscribe({
        next: data => {
          this.items.push(data);
          console.log('add item', data);
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
      //this.storageService.addItem(this.item);
      console.log('form item to add', this.itemToAdd);
      this.addItemToShoppingList();
      this.clearForm();
    }
  }

  openAddModal(itemAddModal: TemplateRef<any>) {
    this.itemToAdd = new Item();
    this.modalService.open(itemAddModal, {ariaLabelledBy: 'modal-basic-title'});
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

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
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
}
