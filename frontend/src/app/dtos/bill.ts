import {Item} from './item';
import {User} from './user';

export interface Bill {
  id: number;
  registerId: number;
  groceries: Set<Item>;
  notes: string;
  names: Set<ApplikationUser>;
  notPaidNames: Set<ApplikationUser>;
  sum: number;
  sumPerPerson: number;
  date: Date;
  nameList: string;
  notPaidNameList: string;
}
