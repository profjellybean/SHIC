import {Component, OnInit, TemplateRef} from '@angular/core';
import {MessageService} from '../../services/message.service';
import {ShoppingListService} from '../../services/shopping-list.service';
import {Item} from '../../dtos/item';
import {ItemStorage} from '../../dtos/itemStorage';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ShoppingList} from '../../dtos/shopping-list';
import {User} from '../../dtos/user';
import jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-shopping-list',
  templateUrl: './shopping-list.component.html',
  styleUrls: ['./shopping-list.component.scss']
})
export class ShoppingListComponent implements OnInit {

  error = false;
  errorMessage = '';
  submitted = false;
  shoppingList: ShoppingList;

  itemsAdd: Item[] = null;
  itemToAdd: Item = null;
  itemsToBuy: Item[] = [];
  items: Item[] = null;

  user: User = {
    // @ts-ignore
    username: jwt_decode(this.authService.getToken()).sub.trim(),
    password: null,
    id: null,
    currGroup: null,
    privList: null
  };


  constructor(private messageService: MessageService,
              private shoppingListService: ShoppingListService,
              private modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.loadItemsToAdd();
    this.loadItems();
    this.getShoppingList();
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  getShoppingList() {
    this.shoppingListService.getShoppingList().subscribe({
        next: res => {
          console.log(res);
        },
        error: err => {
          console.error(err);
        }
      }
    );
  }

  workOffShoppingList() {
    this.shoppingListService.workOffShoppingList(this.itemsToBuy, 7, this.user.username).subscribe({
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
        console.log('received items', data);
        this.itemsAdd = data;
      }
    });
  }

  loadItems() {
    this.shoppingListService.findAll(7).subscribe({
      next: data => {
        console.log('received items', data);

        this.items = data;
      }
    });
  }

  addItemToShoppingList() {
    console.log('item to add', this.itemToAdd);
    this.itemToAdd.shoppingListId = 7;
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

  addItemForm(form) {
    this.submitted = true;

    if (form.valid) {
      //this.storageService.addItem(this.item);
      console.log('form item to add', this.itemToAdd);
      this.addItemToShoppingList();
      //this.clearForm();
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
}
