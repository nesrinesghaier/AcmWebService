import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {AcmerModule} from "./acmer/acmer.module";
import { HttpClientModule, HttpClient } from '@angular/common/http';
import {AcmerService} from "./acmer/acmer.service";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { MatMenuModule,MatButtonModule} from '@angular/material';


@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    AcmerModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatMenuModule,
  ],
  providers: [AcmerService],
  bootstrap: [AppComponent]
})
export class AppModule { }
