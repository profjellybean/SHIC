import { Component, OnInit } from '@angular/core';
import {Item} from '../../dtos/item';
import {StorageService} from '../../services/storage.service';
import {Params} from '@angular/router';
import {ItemStorage} from '../../dtos/itemStorage';

@Component({
  selector: 'app-storage',
  templateUrl: './storage.component.html',
  styleUrls: ['./storage.component.scss']
})
export class StorageComponent implements OnInit {
  items: Item[];
  searchString='';
  searchItem: Item = {image:null, id: null, storageId: 1, name: null,
    notes: null, expDate: null, amount: 0, locationTag: null, shoppingListId: null, quantity: null};

  constructor(private storageService: StorageService) {
  }

  ngOnInit(): void {
    this.getAllItemsByStorageId({id: 1});
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
        console.log('received items', data);
        this.items = data;
      },
      error: error => {
        console.error(error.message);
      }
    });
  }
}
