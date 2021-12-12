import {Item} from './item';
import {ApplikationUser} from './applikationUser';

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
