import { Component, OnInit } from '@angular/core';
import {Item} from '../../dtos/item';
import {ShoppingListService} from '../../services/shopping-list.service';

@Component({
  selector: 'app-shopping-list-list',
  templateUrl: './shopping-list-list.component.html',
  styleUrls: ['./shopping-list-list.component.scss']
})
export class ShoppingListListComponent implements OnInit {

  items: Item[] = null;
  constructor(
    private shoppingListService: ShoppingListService
  ) { }

  ngOnInit(): void {
    this.loadItems();
  }

  loadItems() {
    this.shoppingListService.findAll(7).subscribe({
      next: data => {
        console.log('received items', data);

        this.items = data;
      }
    });
  }

  public addItem(item: Item) {
    this.items.push(item);
  }

}
