import {Item} from './item';

export class Recipe {
  id: number;
  name: string;
  description: string;
  // TODO add Set as ingredients Set as categories
  ingredients: Item[];
  categories: string[];
  groupId: number;
}
