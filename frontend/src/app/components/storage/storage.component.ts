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


@Component({
  selector: 'app-storage',
  templateUrl: './storage.component.html',
  styleUrls: ['./storage.component.scss']
})
export class StorageComponent implements OnInit {
  user: User = {
    // @ts-ignore
    username: jwt_decode(this.authService.getToken()).sub.trim(),
    password: null,
    id: null,
    currGroup: null,
    privList: null
  };
  error = false;
  errorMessage = '';
  submitted = false;
  searchString='';
  searchItem: Item = {image:null, id: null, storageId: null, name: null,
    notes: null, expDate: null, amount: 0, locationTag: null, shoppingListId: null, quantity: null};

  items: Item[] = null;
  item: Item = new Item();
  itemToAdd: Item = new Item();
  itemsToAdd: Item[];

  unitsOfQuantity: UnitOfQuantity[];



  constructor(private storageService: StorageService,
              private modalService: NgbModal,
              private shoppingListService: ShoppingListService,
              private itemService: ItemService,
              private userService: UserService,
              private authService: AuthService) {
  }

  ngOnInit(): void {
    this.getCurrUser();
    this.loadItemsToAdd();
    this.loadUnitsOfQuantity();
  }

  getCurrUser(){
    this.userService.getCurrentUser({username: this.user.username}).subscribe({
      next: data => {
        console.log('received items6', data);
        this.user = data;
        this.getAllItemsByStorageId({id: this.user.currGroup.storageId});
      },
      error: error => {
        console.error(error.message);
      }
    });
  }


  addItem(item: Item) {
    item.storageId = this.user.currGroup.storageId;
    this.itemToAdd.id = null;
    if(this.itemToAdd.quantity !== undefined) {
      this.itemToAdd.quantity = null;
    }
    console.log('item to add', this.itemToAdd);
    this.storageService.addItem(item).subscribe({
      next: data => {
        this.items.push(item);
        this.getAllItemsByStorageId({id: this.user.currGroup.storageId});
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
        console.log('received items8', data);
        this.itemsToAdd = data;
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
    this.searchString=this.createSearchString();
    this.storageService.searchItems(this.searchString).subscribe({
      next: data => {
        console.log('found data', data);
        this.items = data;
      },
      error: error => {
        console.error(error.message);
      }
    });
  }
  createSearchString(): string{
    this.searchString='?id=';
    if(this.searchItem.storageId!=null){
      this.searchString=this.searchString+'&storageId='+this.searchItem.storageId;
    }else {
      this.searchString=this.searchString+'&storageId=';
    }
    if(this.searchItem.name!= null) {
      if (this.searchItem.name.trim() !== '') {
        this.searchString = this.searchString+'&name=' + this.searchItem.name;
      } else {
        this.searchString = this.searchString+'&name=';
      }
    }else{
      this.searchString = this.searchString+'&name=';
    }
    if(this.searchItem.notes!= null) {
      if (this.searchItem.notes.trim() !== '') {
        this.searchString = this.searchString+'&notes=' + this.searchItem.notes;
      } else {
        this.searchString = this.searchString+'&notes=';
      }
    }else{
      this.searchString = this.searchString+'&notes=';
    }
    if(this.searchItem.amount!=null){
      this.searchString= this.searchString+ '&amount='+this.searchItem.amount;
    }else{
      this.searchString= this.searchString+ '&amount=0';
    }
    if(this.searchItem.locationTag!=null){
      this.searchString=this.searchString+'&locationTag='+this.searchItem.locationTag;
    }else {
      this.searchString=this.searchString+'&locationTag=';
    }
    this.searchString=this.searchString+'&quantity=&image=';
    if(this.searchItem.expDate!=null){
      this.searchString=this.searchString+'&expDate='+this.searchItem.expDate;
    }else {
      this.searchString=this.searchString+'&expDate=';
    }


    return this.searchString;
  }

  private getAllItemsByStorageId(params: Params) {
    this.storageService.getItems(params).subscribe({
      next: data => {
        console.log('received items9', data);
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
