import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {AuthRoutingModule} from './auth-routing.module';
import {NbAuthModule, NbTokenLocalStorage, NbTokenStorage} from '@nebular/auth';
import {NbAlertModule, NbButtonModule, NbCheckboxModule, NbInputModule} from '@nebular/theme';
import {RegisterComponent} from './register/register.component';

@NgModule({
  declarations: [LoginComponent, RegisterComponent],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    NbAlertModule,
    NbInputModule,
    NbButtonModule,
    NbCheckboxModule,
    AuthRoutingModule,

    NbAuthModule,
  ],
  providers: [{provide: NbTokenStorage, useClass: NbTokenLocalStorage}]
})
export class AuthModule {
}
