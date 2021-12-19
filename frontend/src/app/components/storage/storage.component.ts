import {Component, OnInit, TemplateRef} from '@angular/core';
import {Item} from '../../dtos/item';
import {StorageService} from '../../services/storage.service';
import {Params} from '@angular/router';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ItemService} from '../../services/item.service';
import {ShoppingListService} from '../../services/shopping-list.service';


@Component({
  selector: 'app-storage',
  templateUrl: './storage.component.html',
  styleUrls: ['./storage.component.scss']
})
export class StorageComponent implements OnInit {

  error = false;
  errorMessage = '';
  submitted = false;

  items: Item[] = null;
  item: Item = new Item();
  itemToAdd: Item = new Item();
  itemsToAdd: Item[];

  constructor(private storageService: StorageService,
              private modalService: NgbModal,
              private shoppingListService: ShoppingListService,
              private itemService: ItemService) {
  }

  ngOnInit(): void {
    this.getAllItemsByStorageId({id: 1});
    this.loadItemsToAdd();
  }


  addItem(item: Item) {
    this.itemToAdd.storageId = 1;
    this.itemToAdd.id = null;
    console.log('item to add', this.itemToAdd);
    this.storageService.addItem(item).subscribe({
      next: data => {
        this.items.push(item);
        this.getAllItemsByStorageId({id: 1});
        console.log('added Item', data);
      },
      error: error => {
        this.defaultServiceErrorHandling(error);
      }
    });
  }

  addItemForm(form) {
    this.submitted = true;

    if(form.valid) {
      //this.storageService.addItem(this.item);
      console.log('form item to add', this.itemToAdd);
      this.addItem(this.itemToAdd);
      this.clearForm();
    }
  }

  openAddModal(itemAddModal: TemplateRef<any>) {
    this.item = new Item();
    this.modalService.open(itemAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  loadItemsToAdd() {
    this.shoppingListService.findAllItems().subscribe({
      next: data => {
        console.log('received items', data);
        this.itemsToAdd = data;
      }
    });
  }


  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
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

  private getAllItemsByStorageId(params: Params) {
    this.storageService.getItems(params).subscribe({
      next: data => {
        console.log('received items', data);
        this.items = data;
      },
      error: error => {
        console.error(error.message);
      }
    });
  }

  private clearForm() {
    this.item = new Item();
    this.itemToAdd = new Item();
    this.submitted = false;
  }
}
