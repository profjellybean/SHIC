import {Component, OnInit, TemplateRef} from '@angular/core';
import {User} from '../../dtos/user';
import jwt_decode from 'jwt-decode';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {LocationTagService} from '../../services/location-tag.service';
import {UserService} from '../../services/user.service';
import {AuthService} from '../../services/auth.service';
import {ItemService} from '../../services/item.service';
import {UnitOfQuantity} from '../../dtos/unitOfQuantity';
import {StorageService} from '../../services/storage.service';

@Component({
  selector: 'app-unit-of-quantity',
  templateUrl: './unit-of-quantity.component.html',
  styleUrls: ['./unit-of-quantity.component.scss']
})
export class UnitOfQuantityComponent implements OnInit {

  user: User = {
    // @ts-ignore
    username: jwt_decode(this.authService.getToken()).sub.trim(),
    id: null,
    currGroup: null,
    privList: null,
    email: null,
    image: null
  };

  storageId = null;

  error = false;
  errorMessage = '';
  submitted = false;
  unitsOfQuantity: UnitOfQuantity[];
  helpUnityOfQuantity: string;
  unitOfQuantity: UnitOfQuantity = {id: null, name: null};
  nullUnitOfQuantity: UnitOfQuantity = {id: null, name: ''};
  unitOfQuantityToAdd: UnitOfQuantity = this.nullUnitOfQuantity;

  constructor(private modalService: NgbModal,
              private locationTagService: LocationTagService,
              private userService: UserService,
              private authService: AuthService,
              private itemService: ItemService,
              private storageService: StorageService) {
  }

  ngOnInit(): void {
    this.loadUnitsOfQuantity();
    this.getCurrUser();
  }

  openAddModal(unitOfQuantityAddModal: TemplateRef<any>) {
    this.modalService.open(unitOfQuantityAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  loadUnitsOfQuantity() {
    this.storageService.findAllUnitsOfQuantity().subscribe({
      next: data => {
        console.log('received units of quantity', data);
        this.unitsOfQuantity = data;
      }
    });
  }

  getCurrUser() {
    this.userService.getCurrentUser({username: this.user.username}).subscribe({
      next: data => {
        console.log('received items6', data);
        this.user = data;
        this.storageId = this.user.currGroup.storageId;
      },
      error: error => {
        console.error(error.message);
      }
    });
  }

  addUnityOfQuantity() {
    this.itemService.createUnitOfQuantity(this.unitOfQuantityToAdd.name).subscribe({
      next: data => {
        this.unitOfQuantity.name = this.unitOfQuantityToAdd.name;
        this.unitsOfQuantity.push(this.unitOfQuantity);
        this.clearForm();
      },
      error: err => {
        console.log(err);
        this.defaultServiceErrorHandling(err);
      }
    });
  }

  locationTagDefaultCheck(unitOfQuantity: UnitOfQuantity) {
    if (unitOfQuantity.name.trim() === 'kg') {
      return false;
    }
    if (unitOfQuantity.name.trim() === 'g') {
      return false;
    }
    if (unitOfQuantity.name.trim() === 'L') {
      return false;
    }
    if (unitOfQuantity.name.trim() === 'ml') {
      return false;
    }
    if (unitOfQuantity.name.trim() === 'pieces') {
      return false;
    }
    if (unitOfQuantity.name.trim() === 'can') {
      return false;
    }
    if (unitOfQuantity.name.trim() === 'cup') {
      return false;
    }
    if (unitOfQuantity.name.trim() === 'jar') {
      return false;
    }
    if (unitOfQuantity.name.trim() === 'dag') {
      return false;
    }
    return true;
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  private clearForm() {
    this.helpUnityOfQuantity = '';
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
