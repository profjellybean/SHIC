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
import { UserComponent } from './components/user/user.component';
import { ConfirmUserEmailComponent } from './components/confirm-user-email/confirm-user-email.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    RegisterUserComponent,
    StorageComponent,
    RegisterComponent,
    RecipeComponent,
    ShoppingListComponent,
    RecipeDetailComponent,
    RecipeListComponent,
    ConfirmUserEmailComponent,
    StorageAddItemComponent,
    UserComponent,
    ShoppingListAddComponent,
    ShoppingListListComponent,
    StorageAddItemComponent,
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
