import {Item} from './item';

export class Recipe {
  id: number;
  name: string;
  description: string;
  ingredients: Item[];
  categories: string[];
  groupId: number;
}
