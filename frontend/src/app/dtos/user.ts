import {Group} from './group';

export class User {
  id: number;
  username: string;
  currGroup: Group;
  privList: number;
  email: string;

  constructor(id: number, username: string, currGroup: Group, privList: number, email: string) {
    this.id = id;
    this.username = username;
    this.currGroup = currGroup;
    this.privList = privList;
    this.email = email;
  }
}
