import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {httpInterceptorProviders} from './interceptors';
import {CustomItemComponent} from './components/custom-item/custom-item.component';
import {RecipeComponent} from './components/recipe/recipe.component';
import {ShoppingListComponent} from './components/shopping-list/shopping-list.component';
import {StorageComponent} from './components/storage/storage.component';
import {RegisterComponent} from './components/register/register.component';
import {RegisterUserComponent} from './components/registerUser/registerUser.component';
import {RecipeDetailComponent} from './components/recipe-detail/recipe-detail.component';
import {UserComponent} from './components/user/user.component';
import {ConfirmUserEmailComponent} from './components/confirm-user-email/confirm-user-email.component';
import {NgSelectModule} from '@ng-select/ng-select';
import {StatisticComponent} from './components/statistic/statistic.component';
import {LocationTagComponent} from './components/location-tag/location-tag.component';
import {NotificationsComponent} from './components/notifications/notifications.component';
import {UnitOfQuantityComponent} from './components/unit-of-quantity/unit-of-quantity.component';
import {HomepageComponent} from './components/homepage/homepage.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    CustomItemComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    RegisterUserComponent,
    StorageComponent,
    RegisterComponent,
    RecipeComponent,
    ShoppingListComponent,
    RecipeDetailComponent,
    ConfirmUserEmailComponent,
    UserComponent,
    NotificationsComponent,
    StatisticComponent,
    LocationTagComponent,
    UnitOfQuantityComponent,
    HomepageComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
    NgSelectModule,
    BrowserAnimationsModule,
  ],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule {
}
