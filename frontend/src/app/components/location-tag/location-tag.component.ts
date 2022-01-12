import {Component, OnInit, TemplateRef} from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {LocationTagService} from '../../services/location-tag.service';
import {LocationTag} from '../../dtos/locationTag';

@Component({
  selector: 'app-location-tag',
  templateUrl: './location-tag.component.html',
  styleUrls: ['./location-tag.component.scss']
})
export class LocationTagComponent implements OnInit {

  error = false;
  errorMessage = '';
  submitted = false;

  storageId = 1;
  nullLocationTag: LocationTag = {id: 10000, name: null, storageId: this.storageId};
  locationTagToAdd: LocationTag = this.nullLocationTag;
  locationTags: LocationTag[] = null;

  constructor(private modalService: NgbModal,
              private locationTagService: LocationTagService) { }

  ngOnInit(): void {
    this.loadLocationTags();
  }


  loadLocationTags() {
    this.locationTagService.getLocationTags({storageId: this.storageId}).subscribe({
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

  addLocationTag() {
    this.locationTagService.saveLocationTag(this.locationTagToAdd).subscribe(
      {
        next: data => {
          console.log('successfully added custom locationTag', data);
          this.locationTags.push(this.locationTagToAdd);
        },
        error: err => {
          console.log(err);
          this.defaultServiceErrorHandling(err);
        }
      });
  }

  addLocationTagForm(form) {
    this.submitted = true;

    if(form.valid) {
      console.log('form locationTag to add', this.locationTagToAdd);
      this.addLocationTag();
      this.clearForm();
    }
  }

  openAddModal(itemAddModal: TemplateRef<any>) {
    this.modalService.open(itemAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  private clearForm() {
    this.locationTagToAdd = this.nullLocationTag;
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
