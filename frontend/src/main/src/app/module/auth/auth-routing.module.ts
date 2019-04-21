import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from '@angular/router';
import {NbAuthComponent} from '@nebular/auth';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from "./register/register.component";

export const routes: Routes = [
  {
    path: '',
    component: NbAuthComponent,
    children: [
      {
        path: 'login',
        component: LoginComponent, // <---
      },
      {
        path: 'register',
        component: RegisterComponent, // <---
      },
    ],
  },
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ],
  exports: [RouterModule],
})
export class AuthRoutingModule {
}
