import {UnitOfQuantity} from './unitOfQuantity';

export class Item {
  id: number;
  name: string;
  quantity: UnitOfQuantity;
  notes: string;
  image: ImageBitmap;
  expDate: Date;
  amount: number;
  locationTag: string;
  storageId: number;
  shoppingListId: number;
}
