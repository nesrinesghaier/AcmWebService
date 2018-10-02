import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {AcmerModule} from "./acmer/acmer.module";
import { HttpClientModule, HttpClient } from '@angular/common/http';
import {AcmerService} from "./acmer/service/acmer.service";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { MatMenuModule,MatButtonModule} from '@angular/material';
import { SlimLoadingBarModule } from 'ng2-slim-loading-bar';
import { LoginComponent } from './login/login.component';
import {FormsModule, NgForm} from "@angular/forms";
import {AuthenticationService} from "./login/service/authentication.service";


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    AcmerModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatMenuModule,
    SlimLoadingBarModule,
    FormsModule,
  ],
  providers: [AcmerService,AuthenticationService],
  bootstrap: [AppComponent]
})
export class AppModule { }
