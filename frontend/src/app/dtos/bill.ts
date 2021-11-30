import {Item} from './item';
import {User} from './user';

export interface Bill {
  id: number;
  groceries: Map<number, Item>;
  notes: string;
  names: Map<number, User>;
  notPaidNames: Map<number, User>;
  sum: number;
  sumPerPerson: number;
  date: Date;
}
