import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {AcmerModule} from "./acmer/acmer.module";
import { HttpClientModule, HttpClient } from '@angular/common/http';
import {AcmerService} from "./acmer/acmer.service";
import {AcmerAddComponent} from "./acmer/acmer-add/acmer-add.component";
import {AcmerListComponent} from "./acmer/acmer-list/acmer-list.component";
import {AcmerDetailsComponent} from "./acmer/acmer-details/acmer-details.component";

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    AcmerModule,
  ],
  providers: [AcmerService],
  bootstrap: [AppComponent]
})
export class AppModule { }
