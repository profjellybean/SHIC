import {Group} from './group';


export interface User {
  id: number;
  username: string;
  email: string;
  currGroup: Group;
  privList: number;
  image: any;
}
