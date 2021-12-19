import {UnitOfQuantity} from './unitOfQuantity';

export interface Item {
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
