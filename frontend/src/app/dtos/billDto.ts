import {Item} from './item';
import {User} from './user';


export class BillDto {
  id: number;
  registerId: number;
  groceries: Item[] = [];
  notes: string;
  names: User[] = [];
  notPaidNames: User[] = [];
  sum: number;
  sumPerPerson: number;
  date: Date;
}
