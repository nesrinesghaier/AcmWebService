import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserListComponent } from './user-list/user-list.component';
import { UserCreateComponent } from './user-create/user-create.component';

const routes: Routes = [
  {path: 'acmers', component: UserListComponent},
  {path: 'acmers/:handle', component: UserCreateComponent},
  {path: 'acmers/:handle/:add', component: UserCreateComponent}
];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
