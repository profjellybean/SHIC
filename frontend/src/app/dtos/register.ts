import {Bill} from './bill';

export interface Register {
  id: number;
  bills: Map<number, Bill>;
  monthlyPayment: number;
  monthlyBudget: number;
}
