import { Component, OnInit } from '@angular/core';
import {Item} from '../../dtos/item';
import {StorageService} from '../../services/storage.service';
import {Params} from '@angular/router';

@Component({
  selector: 'app-storage',
  templateUrl: './storage.component.html',
  styleUrls: ['./storage.component.scss']
})
export class StorageComponent implements OnInit {
  items: Item[];

  constructor(private storageService: StorageService) {
  }

  ngOnInit(): void {
    this.getAllItemsByStorageId({id: 1});
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
