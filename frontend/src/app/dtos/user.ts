import {Group} from './group';

export interface User {
  id: number;
  username: string;
  password: string;
  currGroup: Group;
  privList: number;
}
