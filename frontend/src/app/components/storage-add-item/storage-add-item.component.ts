import {Component, OnInit, TemplateRef} from '@angular/core';
import {MessageService} from '../../services/message.service';
import {ItemService} from '../../services/item.service';
import {Item} from '../../dtos/item';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

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

  constructor( private messageService: MessageService,
               private itemService: ItemService,
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

  addItem(form) {
    this.submitted = true;

    if(form.valid) {
      this.itemService.addItem(this.item);
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
}
