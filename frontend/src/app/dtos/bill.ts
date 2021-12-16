import {Item} from './item';
import {ApplicationUser} from './applicationUser';

export interface Bill {
  id: number;
  registerId: number;
  groceries: Set<Item>;
  notes: string;
  names: Set<ApplicationUser>;
  notPaidNames: Set<ApplicationUser>;
  sum: number;
  sumPerPerson: number;
  date: Date;
  nameList: string;
  notPaidNameList: string;
}
