import {Bill} from './bill';

export interface Register {
  id: number;
  bills: Bill[];
  monthlyPayment: number;
  monthlyBudget: number;
}
