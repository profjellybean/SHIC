import { Component, OnInit } from '@angular/core';
import {ShoppingListService} from '../../services/shoppinglist.service';

import {MessageService} from '../../services/message.service';


@Component({
  selector: 'app-shopping-list',
  templateUrl: './shopping-list.component.html',
  styleUrls: ['./shopping-list.component.scss']
})
export class ShoppingListComponent implements OnInit {

  error = false;
  errorMessage = '';
  constructor(private shoppingListService: ShoppingListService,private messageService: MessageService) { }

  itemsAdd: Item[] = null;
  itemToAdd: Item = null;

  constructor(private messageService: MessageService,
              private shoppingListService: ShoppingListService) { }

  ngOnInit(): void {
    this.loadItemsToAdd();
  }

  getShoppingList(){
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
  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  loadItemsToAdd() {
    this.shoppingListService.findAllItems().subscribe({
      next: data => {
        console.log('received items', data);
        this.itemsAdd = data;
      }
    });
  }

  addItemToShoppingList() {
    console.log('item to add', this.itemToAdd);
    this.itemToAdd.shoppingListId = 1;
    this.itemToAdd.id = null;
    console.log('item to add', this.itemToAdd);
    this.shoppingListService.addItemToShoppingList(this.itemToAdd);
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
