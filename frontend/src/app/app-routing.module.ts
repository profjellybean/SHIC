import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {MessageComponent} from './components/message/message.component';
import {StorageComponent} from './components/storage/storage.component';
import {RegisterUserComponent} from './components/registerUser/registerUser.component';
import {ShoppingListComponent} from './components/shopping-list/shopping-list.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'registerUser', component: RegisterUserComponent},
  {path: 'message', canActivate: [AuthGuard], component: MessageComponent},
  {path: 'storage', component: StorageComponent},
  {path: 'shopping-list', component: ShoppingListComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
