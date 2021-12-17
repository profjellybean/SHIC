import {Item} from './item';
import {User} from './User';

export interface Bill {
  id: number;
  registerId: number;
  groceries: Set<Item>;
  notes: string;
  names: Set<User>;
  notPaidNames: Set<User>;
  sum: number;
  sumPerPerson: number;
  date: Date;
  nameList: string;
  notPaidNameList: string;
}
