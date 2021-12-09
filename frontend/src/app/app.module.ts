import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {MessageComponent} from './components/message/message.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {httpInterceptorProviders} from './interceptors';
import { RecipeComponent } from './components/recipe/recipe.component';
import { ShoppingListComponent } from './components/shopping-list/shopping-list.component';
import { StorageComponent } from './components/storage/storage.component';
import {RegisterComponent} from './components/register/register.component';
import {RegisterUserComponent} from './components/registerUser/registerUser.component';
import { RecipeDetailComponent } from './components/recipe-detail/recipe-detail.component';
import { RecipeListComponent } from './components/recipe-list/recipe-list.component';
import { ShoppingListAddComponent } from './components/shopping-list-add/shopping-list-add.component';
import { ShoppingListListComponent } from './components/shopping-list-list/shopping-list-list.component';
import { StorageAddItemComponent } from './components/storage-add-item/storage-add-item.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    RegisterUserComponent,
    MessageComponent,
    StorageComponent,
    RegisterComponent,
    RecipeComponent,
    ShoppingListComponent,
    RecipeDetailComponent,
    RecipeListComponent,
    StorageAddItemComponent,
    ShoppingListAddComponent,
    ShoppingListListComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
  ],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule {
}
