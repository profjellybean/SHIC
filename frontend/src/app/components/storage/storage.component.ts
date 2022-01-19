import {Component, OnInit, TemplateRef} from '@angular/core';
import {Item} from '../../dtos/item';
import {StorageService} from '../../services/storage.service';
import {Params} from '@angular/router';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ItemService} from '../../services/item.service';
import {ShoppingListService} from '../../services/shopping-list.service';
import {UnitOfQuantity} from '../../dtos/unitOfQuantity';
import {UserService} from '../../services/user.service';
import {User} from '../../dtos/user';
import jwt_decode from 'jwt-decode';
import {AuthService} from '../../services/auth.service';
import {LocationTag} from '../../dtos/locationTag';
import {LocationTagService} from '../../services/location-tag.service';


@Component({
  selector: 'app-storage',
  templateUrl: './storage.component.html',
  styleUrls: ['./storage.component.scss']
})
export class StorageComponent implements OnInit {
  user: User = {
    // @ts-ignore
    username: jwt_decode(this.authService.getToken()).sub.trim(),
    id: null,
    currGroup: null,
    privList: null,
    email: null,
    image: null
  };
  error = false;
  errorMessage = '';
  submitted = false;
  searchString = '';
  searchItem: Item = {
    image: null, id: null, storageId: null, name: null,
    notes: null, expDate: null, amount: 0, locationTag: null, shoppingListId: null, quantity: null
  };
  trash: string;
  trashBoolean: boolean;

  items: Item[] = null;
  item: Item = new Item();
  nullQuantity: UnitOfQuantity = {id: null, name: null};
  nullItem: Item = {
    image: null, id: null, storageId: null, name: null,
    notes: null, expDate: null, amount: 0, locationTag: null, shoppingListId: null, quantity: this.nullQuantity
  };

  absolutNullItem: Item = {
    image: null, id: null, storageId: null, name: null,
    notes: null, expDate: null, amount: null, locationTag: null, shoppingListId: null, quantity: this.nullQuantity
  };

  itemToAdd: Item = this.nullItem;
  itemsToAdd: Item[];

  itemToUpdate: Item = this.absolutNullItem;

  shopAgainAmount: number;
  shopAgainNotes: string;

  unitsOfQuantity: UnitOfQuantity[];
  locationTags: LocationTag[] = null;



  constructor(private storageService: StorageService,
              private modalService: NgbModal,
              private shoppingListService: ShoppingListService,
              private itemService: ItemService,
              private userService: UserService,
              private authService: AuthService,
              private locationTagService: LocationTagService) {
  }

  ngOnInit(): void {
    this.getCurrUser();
    this.loadItemsToAdd();
    this.loadUnitsOfQuantity();
    this.trash='true';

  }

  getCurrUser() {
    this.userService.getCurrentUser({username: this.user.username}).subscribe({
      next: data => {
        console.log('received items6', data);
        this.user = data;
        this.getAllItemsByStorageId({id: this.user.currGroup.storageId});
        this.searchItem.storageId = this.user.currGroup.storageId;
        this.loadLocationTags(data.currGroup.storageId);
      },
      error: error => {
        console.error(error.message);
      }
    });
  }

  loadLocationTags(storageId: number) {
    this.locationTagService.getLocationTags({storageId}).subscribe({
        next: data => {
          console.log('successfully loaded custom location tags', data);
          this.locationTags = data;
        },
        error: err => {
          console.log(err);
          this.defaultServiceErrorHandling(err);
        }
      }
    );
  }

  addItem(item: Item) {
    item.storageId = this.user.currGroup.storageId;
    item.id = null;
    if (item.quantity === undefined) {
      item.quantity = null;
    }
    if (item.amount === undefined) {
      item.amount = null;
    }
    this.unitsOfQuantity.forEach(unit => {
      if (item.quantity.id === unit.id) {
        item.quantity.name = unit.name;
      }
    });
    console.log('item to add', this.itemToAdd);
    this.storageService.addItem(item).subscribe({
      next: data => {
        //this.items.push(item);
        this.getAllItemsByStorageId({id: this.user.currGroup.storageId});
        this.itemToAdd = this.nullItem;
        console.log('added Item', data);

        // todo dont reload every time
        this.loadItemsToAdd();

      },
      error: error => {
        this.defaultServiceErrorHandling(error);
      }
    });
  }

  addItemForm(form) {
    this.submitted = true;

    if (form.valid) {
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

  setUpdateItem(item: Item) {
    this.itemToUpdate.id = item.id;
    this.itemToUpdate.name = item.name;
    this.itemToUpdate.quantity = item.quantity;
    this.itemToUpdate.notes = item.notes;
    this.itemToUpdate.image = item.image;
    this.itemToUpdate.expDate = item.expDate;
    this.itemToUpdate.amount = item.amount;
    this.itemToUpdate.locationTag = item.locationTag;
    this.itemToUpdate.storageId = item.storageId;
    this.itemToUpdate.shoppingListId = item.shoppingListId;
  }

  updateItemForm(form, item: Item) {
    this.submitted = true;

    if (form.valid) {
      //this.storageService.addItem(this.item);
      console.log('form item to update', this.itemToUpdate);
      this.updateItem(this.itemToUpdate, item);
      this.clearForm();
    }
  }

  updateItem(itemToUpdate: Item, item: Item) {
    item.id = itemToUpdate.id;
    item.name = itemToUpdate.name;
    item.quantity = itemToUpdate.quantity;
    item.notes = itemToUpdate.notes;
    item.image = itemToUpdate.image;
    item.expDate = itemToUpdate.expDate;
    item.amount = itemToUpdate.amount;
    item.locationTag = itemToUpdate.locationTag;
    item.storageId = itemToUpdate.storageId;
    item.shoppingListId = itemToUpdate.shoppingListId;
    if (item.quantity === undefined) {
      item.quantity = null;
    }
    if (item.amount === undefined) {
      item.amount = null;
    }
    this.unitsOfQuantity.forEach(unit => {
      if (item.quantity.id === unit.id) {
        item.quantity.name = unit.name;
      }
    });
    console.log('item to update', this.itemToUpdate);
    this.storageService.updateItem(item).subscribe({
      next: data => {
        this.getAllItemsByStorageId({id: this.user.currGroup.storageId});
        this.itemToUpdate = this.absolutNullItem;
        console.log('updated Item', data);

        // todo dont reload every time
        this.loadItemsToAdd();

      },
      error: error => {
        this.defaultServiceErrorHandling(error);
      }
    });
  }

  loadItemsToAdd() {
    //this.shoppingListService.findAllItems().subscribe({
    this.itemService.findAllItemsForGroup().subscribe({
      next: data => {
        console.log('received items to add', data);

        this.itemsToAdd = data;
      },
      error: error => {
        this.defaultServiceErrorHandling(error);
      }
    });
  }

  loadUnitsOfQuantity() {
    this.storageService.findAllUnitsOfQuantity().subscribe({
      next: data => {
        console.log('received units of quantity', data);
        this.unitsOfQuantity = data;
      }
    });
  }


  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  searchItems() {
    this.searchString = this.createSearchString();
    this.storageService.searchItems(this.searchString).subscribe({
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

  deleteItem(item: Item) {
    if (this.trash==='true'){
      this.trashBoolean= true;
    }
    if(this.trash==='false'){
      this.trashBoolean=false;
    }
    this.storageService.deleteItemFromStorage({itemId: item.id, trash: this.trash}).subscribe(
      {
        next: data => {
          console.log(data);
          this.removeItemFromStorage(item);
        },
        error: err => {
          console.error(err);
          this.defaultServiceErrorHandling(err);
        }
      }
    );
  }

  putOnPublicShoppinglist(item: Item) {
    console.log('hey i am public ', item);
    item.storageId = null;
    item.amount = this.shopAgainAmount;
    this.shopAgainAmount = 0;
    item.notes = this.shopAgainNotes;
    this.shopAgainNotes = '';
    this.shoppingListService.addToPublicShoppingList(item).subscribe(
      {
        next: data => {
          console.log('i got in public');
          this.deleteItem(item);
        },
        error: err => {
          this.defaultServiceErrorHandling(err);
        }
      }
    );
  }

  putOnPrivateShoppinglist(item: Item) {
    console.log('hey i am private ', item);
    item.storageId = null;
    item.amount = this.shopAgainAmount;
    this.shopAgainAmount = 0;
    item.notes = this.shopAgainNotes;
    this.shopAgainNotes = '';
    this.shoppingListService.addToPrivateShoppingList(item).subscribe(
      {
        next: data => {
          console.log('i got in private');
          this.deleteItem(item);
        },
        error: err => {
          this.defaultServiceErrorHandling(err);
        }
      }
    );
  }

  private removeItemFromStorage(item: Item) {
    for (let i = 0; i < this.items.length; i++) {
      if(this.items[i].id === item.id) {
        this.items.splice(i, 1);
      }
    }
  }

  private createSearchString(): string {
    this.searchString = '?id=';
    if (this.searchItem.storageId != null) {
      this.searchString = this.searchString + '&storageId=' + this.searchItem.storageId;
    } else {
      this.searchString = this.searchString + '&storageId=';
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

  private getAllItemsByStorageId(params: Params) {
    this.storageService.getItems(params).subscribe({
      next: data => {
        console.log('received items in storage', data);

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
