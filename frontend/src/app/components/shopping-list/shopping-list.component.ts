import { Component, OnInit } from '@angular/core';
import {ShoppingListService} from '../../services/shoppinglist.service';

import {Component, OnInit, TemplateRef} from '@angular/core';
import {MessageService} from '../../services/message.service';
import {ShoppingListService} from '../../services/shopping-list.service';
import {Item} from '../../dtos/item';
import {ItemStorage} from '../../dtos/itemStorage';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

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

  constructor(private messageService: MessageService,
              private shoppingListService: ShoppingListService,
              private modalService: NgbModal) { }

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
    this.shoppingListService.addItemToShoppingList(this.itemToAdd).subscribe({
      next: data => {
        console.log('add item', data);
      },
      error: err => {
        this.defaultServiceErrorHandling(err);
      }
    });
  }

  addItemForm(form) {
    this.submitted = true;

    if(form.valid) {
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
