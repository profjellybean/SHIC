import {Group} from './group';

export interface User {
  id: number;
  username: string;
  currGroup: Group;
  privList: number;
}
