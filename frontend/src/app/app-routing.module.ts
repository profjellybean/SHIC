import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {MessageComponent} from './components/message/message.component';
import {StorageComponent} from './components/storage/storage.component';
import {RegisterComponent} from './components/register/register.component';
import {RecipeComponent} from './components/recipe/recipe.component';
import {ShoppingListComponent} from './components/shopping-list/shopping-list.component';
import {RecipeDetailComponent} from './components/recipe-detail/recipe-detail.component';
import {RegisterUserComponent} from './components/registerUser/registerUser.component';
import {UserComponent} from './components/user/user.component';
import {HomeComponent} from './components/home/home.component';
import {ConfirmUserEmailComponent} from './components/confirm-user-email/confirm-user-email.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'registerUser', component: RegisterUserComponent},
  {path: 'message', canActivate: [AuthGuard], component: MessageComponent},
  {path: 'storage', component: StorageComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'confirm', component: ConfirmUserEmailComponent},
  {path: 'storage', component: StorageComponent},
  {path: 'recipe/:id', canActivate: [AuthGuard], component: RecipeDetailComponent},
  {path: 'recipe', canActivate: [AuthGuard], component: RecipeComponent},
  {path: 'shopping-list', canActivate: [AuthGuard], component: ShoppingListComponent},
  {path: 'user', canActivate: [AuthGuard], component: UserComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
