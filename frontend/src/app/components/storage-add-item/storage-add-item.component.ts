import { Component, OnInit } from '@angular/core';
import {MessageService} from '../../services/message.service';
import {ItemService} from '../../services/item.service';
import {Item} from '../../dtos/item';

@Component({
  selector: 'app-storage-add-item',
  templateUrl: './storage-add-item.component.html',
  styleUrls: ['./storage-add-item.component.scss']
})
export class StorageAddItemComponent implements OnInit {

  error = false;
  errorMessage = '';

  items: Item[] = null;
  item: Item = null;

  constructor( private messageService: MessageService,
               private itemService: ItemService) { }

  ngOnInit(): void {
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
        console.error(error.message);
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
}
