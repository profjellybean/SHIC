import {Component, OnInit, TemplateRef} from '@angular/core';
import {ItemService} from '../../services/item.service';
import {Item} from '../../dtos/item';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {StorageService} from '../../services/storage.service';

@Component({
  selector: 'app-storage-add-item',
  templateUrl: './storage-add-item.component.html',
  styleUrls: ['./storage-add-item.component.scss']
})
export class StorageAddItemComponent implements OnInit {

  error = false;
  errorMessage = '';
  submitted = false;

  items: Item[] = null;
  item: Item = new Item();
  testItem: Item = new Item();

  constructor(
               private itemService: ItemService,
               private storageService: StorageService,
               private modalService: NgbModal) { }

  ngOnInit(): void {
    // TODO replace with search
    this.loadAllItems();
  }

  loadAllItems() {
    this.itemService.findAll().subscribe({
      next: data => {
        // TODO add error
        console.log('received items', data);
        this.items = data;
      },
      error: error => {
        this.defaultServiceErrorHandling(error);
      }
    });
  }

  addItem(item: Item) {
    this.setItemTestData();
    item = this.testItem;
    console.log('adding Test items', item); // TODO remove/ edit
    this.storageService.addItem(item).subscribe({
      next: data => {
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
      this.addItem(this.testItem);
      this.clearForm();
    }
  }

  openAddModal(itemAddModal: TemplateRef<any>) {
    this.item = new Item();
    this.modalService.open(itemAddModal, {ariaLabelledBy: 'modal-basic-title'});
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

  private clearForm() {
    this.item = new Item();
    this.submitted = false;
  }

  private setItemTestData() {
    this.testItem.name = 'testName';
    this.testItem.notes = 'testNotes';
    this.testItem.storageId = 1;
    this.testItem.amount = 10;
    this.testItem.quantity = null;
    // this.testItem.locationTag = 1;
    // this.testItem.expDate = null;
    // this.testItem.image = null;
  }
}
