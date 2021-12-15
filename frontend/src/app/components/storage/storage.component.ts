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
  searchItem: ItemStorage = {id: null, storageId: 1, name: null, notes: null, expDate: null, amount: 0, locationTag: null};

  constructor(private storageService: StorageService) {
  }

  ngOnInit(): void {
    this.getAllItemsByStorageId({id: 1});
  }

 searchItems(params: Params) {
    this.storageService.searchItems(params).subscribe({
      next: data => {
        console.log('found data', data);
        this.items = data;
      },
      error: error => {
        console.error(error.message);
      }
    });
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
