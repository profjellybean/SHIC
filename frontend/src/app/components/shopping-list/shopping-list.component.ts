import { Component, OnInit } from '@angular/core';
import {MessageService} from '../../services/message.service';
import {ShoppingListService} from '../../services/shopping-list.service';
import {Item} from '../../dtos/item';
import {ItemStorage} from '../../dtos/itemStorage';

@Component({
  selector: 'app-shopping-list',
  templateUrl: './shopping-list.component.html',
  styleUrls: ['./shopping-list.component.scss']
})
export class ShoppingListComponent implements OnInit {

  error = false;
  errorMessage = '';

  itemsAdd: Item[] = null;
  itemToAdd: Item = null;

  constructor(private messageService: MessageService,
              private shoppingListService: ShoppingListService) { }

  ngOnInit(): void {
    this.loadItemsToAdd();
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
    this.shoppingListService.addItemToShoppingList(this.itemToAdd).subscribe({
      next: data => {
        console.log('add item', data);
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
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }

}
