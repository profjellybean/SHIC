import {Component, Injectable, OnInit} from '@angular/core';
import { trigger, transition, style, animate, query, stagger } from '@angular/animations';


const listAnimation = trigger('listAnimation', [
  transition('* <=> *', [
    query(':enter',
      [style({ opacity: 0 }), stagger('60ms', animate('600ms ease-out', style({ opacity: 1 })))],
      { optional: true }
    ),
    query(':leave',
      animate('200ms', style({ opacity: 0 })),
      { optional: true }
    )
  ])
]);

@Injectable({
  providedIn: 'root'
})

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss'],
  animations: [listAnimation]
})

export class NotificationsComponent{

  static notificatiions: NotificationMessage[] = [];
  error: string;
  collectorStarted: boolean;

  constructor() { }


  public pushSuccess(msgString: string) {
    NotificationsComponent.notificatiions.push({msg:msgString,type:true});
    if(!this.collectorStarted){
      setTimeout( () => this.collectOld(), 5000);
      this.collectorStarted = true;
    }
  }

  public pushFailure(msgString: string) {
    NotificationsComponent.notificatiions.push({msg:msgString,type:false});
    if(!this.collectorStarted){
      setTimeout( () => this.collectOld(), 5000);
      this.collectorStarted = true;
    }
  }

  public vanishError(): void {
    this.error = null;
  }

  public  getNotifications(): NotificationMessage[]{
    return NotificationsComponent.notificatiions;
  }

  public vanishSuccess(i: number): void {
    console.log(NotificationsComponent.notificatiions.length);
    if(NotificationsComponent.notificatiions.length > i){
      NotificationsComponent.notificatiions.splice(0, 1);
    }
  }

  private collectOld(){

    if(NotificationsComponent.notificatiions.length > 0){
      this.vanishSuccess(0);
      setTimeout( ()=> this.collectOld(), 5000);
    }else{
      this.collectorStarted = false;
    }


  }
  private showError(msg: string) {
    this.error = msg;
  }


}


export interface NotificationMessage{
  msg: string;
  type: boolean;
}
