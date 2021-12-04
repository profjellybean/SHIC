import {Item} from './item';
import {User} from './user';

export interface Bill {
  id: number;
  groceries: string[];
  notes: string;
  names: User[];
  notPaidNames: User[];
  sum: number;
  sumPerPerson: number;
  date: Date;
}
