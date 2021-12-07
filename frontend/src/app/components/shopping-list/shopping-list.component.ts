import { Component, OnInit } from '@angular/core';
import {ShoppingListService} from '../../services/shoppinglist.service';


@Component({
  selector: 'app-shopping-list',
  templateUrl: './shopping-list.component.html',
  styleUrls: ['./shopping-list.component.scss']
})
export class ShoppingListComponent implements OnInit {

  constructor(private shoppingListService: ShoppingListService) { }

  ngOnInit(): void {
  }

  getShoppingList(){
    this.shoppingListService.getShoppingList().subscribe({
        next: res => {
          console.log(res);
        },
        error: err => {
          console.error(err);
        }
      }
    );
  }
}
