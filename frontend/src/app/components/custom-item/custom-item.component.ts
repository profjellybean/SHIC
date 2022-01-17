import {Component, OnInit, TemplateRef} from '@angular/core';
import {ItemService} from '../../services/item.service';
import {Item} from '../../dtos/item';
import {UnitOfQuantity} from '../../dtos/unitOfQuantity';
import {StorageService} from '../../services/storage.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-custom-item',
  templateUrl: './custom-item.component.html',
  styleUrls: ['./custom-item.component.scss']
})
export class CustomItemComponent implements OnInit {

  error = false;
  errorMessage = '';
  submitted = false;

  nullQuantity: UnitOfQuantity = {id: null, name: null};
  nullItem: Item = {image:null, id: null, storageId: null, name: null,
    notes: null, expDate: null, amount: 0, locationTag: null, shoppingListId: null, quantity: this.nullQuantity};

  itemToEdit: Item = null;
  itemToAdd: Item = this.nullItem;
  items: Item[] = null;
  unitsOfQuantity: UnitOfQuantity[];
  helpUnityOfQuantity: string;
  unitOfQuantity: UnitOfQuantity= {id: null, name: null};

  testQuantity: UnitOfQuantity = {id: 5, name: 'pieces'};
  testItemToEdit: Item = {image:null, id: 1, storageId: null, name: 'Edited Avocado 2',
    notes: null, expDate: null, amount: 0, locationTag: null, shoppingListId: null, quantity: this.testQuantity};


  constructor(private itemService: ItemService,
              private modalService: NgbModal,
              private storageService: StorageService) { }

  ngOnInit(): void {
    this.loadCustomItems();
    this.loadUnitsOfQuantity();
  }

  editCustomItem() {
    this.itemService.editCustomItem(this.itemToEdit).subscribe({
        next: data => {
          console.log('successfully edited custom Items', data);
          // TODO dont reload every time
          // this.loadCustomItems();
        },
        error: err => {
          console.log(err);
          this.defaultServiceErrorHandling(err);
        }
    });
  }

  addCustomItem() {
    this.itemService.addCustomItem(this.itemToAdd).subscribe({
        next: data => {
          console.log('successfully added custom Item', data);
          // TODO dont reload every time
          //this.loadCustomItems();
          this.items.push(data);
        },
        error: err => {
          console.log(err);
          this.defaultServiceErrorHandling(err);
        }
    });
  }

  addUnityOfQuantity(){
    this.itemService.createUnitOfQuantity(this.helpUnityOfQuantity).subscribe({
      next: data => {
        this.unitOfQuantity.name=this.helpUnityOfQuantity;
        this.unitsOfQuantity.push(this.unitOfQuantity);
        this.helpUnityOfQuantity='';
      },
      error: err => {
        console.log(err);
        this.defaultServiceErrorHandling(err);
      }
    });
  }

  loadCustomItems() {
    this.itemService.findAllItemsByGroupId().subscribe({
        next: data => {
          console.log('successfully loaded custom Items', data);
          this.items = data;
        },
        error: err => {
          console.log(err);
          this.defaultServiceErrorHandling(err);
        }
      }
    );
  }

  loadUnitsOfQuantity() {
    this.storageService.findAllUnitsOfQuantity().subscribe({
      next: data => {
        console.log('received units of quantity', data);
        this.unitsOfQuantity = data;
      }
    });
  }

  editItemForm(form) {
    this.submitted = true;

    if(form.valid) {
      console.log('form item to edit', this.itemToEdit);
      this.editCustomItem();
      this.clearForm();
    }
  }

  addItemForm(form) {
    this.submitted = true;

    if(form.valid) {
      console.log('form item to add', this.itemToAdd);
      this.addCustomItem();
      this.clearForm();
    }
  }

  openAddModal(itemAddModal: TemplateRef<any>) {
    this.modalService.open(itemAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  openEditModal(itemAddModal: TemplateRef<any>, item: Item) {
    this.itemToEdit = item;
    this.modalService.open(itemAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  private clearForm() {
    this.itemToEdit = this.nullItem;
    this.itemToAdd = this.nullItem;
    this.submitted = false;

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
