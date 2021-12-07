import {Bill} from './bill';

export interface Register {
  id: number;
  bills: Set<Bill>;
  monthlyPayments: number;
  monthlyBudget: number;
}
