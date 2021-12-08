import {Item} from './item';
import {User} from './user';
import {ItemStorage} from './itemStorage';

export interface Bill {
  id: number;
  registerId: number;
  groceries: Set<ItemStorage>;
  notes: string;
  names: Set<User>;
  notPaidNames: Set<User>;
  sum: number;
  sumPerPerson: number;
  date: Date;
  nameList: string;
  notPaidNameList: string;
}
