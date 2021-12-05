import { Component, OnInit } from '@angular/core';
import {MessageService} from '../../services/message.service';
import {Recipe} from '../../dtos/recipe';

@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrls: ['./recipe.component.scss']
})
export class RecipeComponent implements OnInit {

  fakeRecipe: Recipe = {
    id: 1, name: 'fakeRecipe', description: 'this recipe is not real'
  };

  error = false;
  errorMessage = '';

  constructor(private messageService: MessageService) { }

  ngOnInit(): void {
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }
}
