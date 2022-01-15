import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {StorageComponent} from './components/storage/storage.component';
import {RegisterComponent} from './components/register/register.component';
import {RecipeComponent} from './components/recipe/recipe.component';
import {ShoppingListComponent} from './components/shopping-list/shopping-list.component';
import {RecipeDetailComponent} from './components/recipe-detail/recipe-detail.component';
import {RegisterUserComponent} from './components/registerUser/registerUser.component';
import {StorageAddItemComponent} from './components/storage-add-item/storage-add-item.component';
import {UserComponent} from './components/user/user.component';
import {HomeComponent} from './components/home/home.component';
import {ConfirmUserEmailComponent} from './components/confirm-user-email/confirm-user-email.component';
import {CustomItemComponent} from './components/custom-item/custom-item.component';
import {ConfirmUserEmailChangedComponent} from './components/confirm-user-email-changed/confirm-user-email-changed.component';


const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'registerUser', component: RegisterUserComponent},
  {path: 'storage', component: StorageComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'confirm', component: ConfirmUserEmailComponent},
  {path: 'confirmNew', component: ConfirmUserEmailChangedComponent},
  {path: 'storage', component: StorageComponent},
  {path: 'storage/add', component: StorageAddItemComponent},
  {path: 'recipe/:id', canActivate: [AuthGuard], component: RecipeDetailComponent},
  {path: 'recipe', canActivate: [AuthGuard], component: RecipeComponent},
  {path: 'shopping-list', canActivate: [AuthGuard], component: ShoppingListComponent},
  {path: 'custom-item', canActivate: [AuthGuard], component: CustomItemComponent},
  {path: 'user', canActivate: [AuthGuard], component: UserComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
