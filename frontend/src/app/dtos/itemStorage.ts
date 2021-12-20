import {UnitOfQuantity} from './unitOfQuantity';

export class ItemStorage {
  //storageId: number;
  shoppingListId: number;
  id: number;
  name: string;
  quantity: UnitOfQuantity;
  notes: string;
  //image: ImageBitmap;
  expDate: Date;
  amount: number;
  locationTag: string;
}
