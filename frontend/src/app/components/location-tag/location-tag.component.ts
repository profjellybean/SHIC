import {Component, OnInit, TemplateRef} from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {LocationTagService} from '../../services/location-tag.service';
import {LocationTag} from '../../dtos/locationTag';
import {User} from '../../dtos/user';
import jwt_decode from 'jwt-decode';
import {UserService} from '../../services/user.service';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-location-tag',
  templateUrl: './location-tag.component.html',
  styleUrls: ['./location-tag.component.scss']
})
export class LocationTagComponent implements OnInit {

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

  storageId = null;
  nullLocationTag: LocationTag = {id: null, name: null, storageId: this.storageId};
  locationTagToAdd: LocationTag = this.nullLocationTag;
  locationTags: LocationTag[] = null;
  locationTagToDelete: LocationTag = null;

  constructor(private modalService: NgbModal,
              private locationTagService: LocationTagService,
              private userService: UserService,
              private authService: AuthService) {
  }

  ngOnInit(): void {
    this.getCurrUser();
  }

  getCurrUser() {
    this.userService.getCurrentUser({username: this.user.username}).subscribe({
      next: data => {
        console.log('received items6', data);
        this.user = data;
        this.storageId = this.user.currGroup.storageId;
        this.nullLocationTag.storageId = this.user.currGroup.storageId;
        this.loadLocationTags(data.currGroup.storageId);
      },
      error: error => {
        console.error(error.message);
      }
    });
  }

  locationTagDefaultCheck(locationTag: LocationTag) {
    if (locationTag.name.trim() === 'shelf') {
      return false;
    }
    if (locationTag.name.trim() === 'freezer') {
      return false;
    }
    if (locationTag.name.trim() === 'fridge') {
      return false;
    }
    return true;
  }


  loadLocationTags(storageId: number) {
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
          this.loadLocationTags(this.storageId);
        },
        error: err => {
          console.log(err);
          this.defaultServiceErrorHandling(err);
        }
      });
  }

  deleteLocationTagById(id: number) {
    this.locationTagService.deleteLocationTag({id}).subscribe(
      {
        next: data => {
          console.log('successfully deleted custom locationTag', data);
          this.loadLocationTags(this.storageId);
        },
        error: err => {
          console.log(err);
          this.defaultServiceErrorHandling(err);
        }
      });
  }

  addLocationTagForm(form) {
    this.submitted = true;

    if (form.valid) {
      console.log('form locationTag to add', this.locationTagToAdd);
      this.addLocationTag();
      this.clearForm();
      console.log('after clear form: ' + this.nullLocationTag.name);
    }
  }

  openAddModal(locationTagAddModal: TemplateRef<any>) {
    this.modalService.open(locationTagAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  openDeleteModal(locationTagDeleteModal: TemplateRef<any>) {
    this.modalService.open(locationTagDeleteModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  private clearForm() {
    this.locationTagToAdd = new LocationTag();
    this.locationTagToAdd.storageId = this.storageId;
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
