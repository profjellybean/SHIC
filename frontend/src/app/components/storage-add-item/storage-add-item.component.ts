import { Component, OnInit } from '@angular/core';
import {MessageService} from '../../services/message.service';

@Component({
  selector: 'app-storage-add-item',
  templateUrl: './storage-add-item.component.html',
  styleUrls: ['./storage-add-item.component.scss']
})
export class StorageAddItemComponent implements OnInit {

  error = false;
  errorMessage = '';

  constructor( private messageService: MessageService ) { }

  ngOnInit(): void {
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
