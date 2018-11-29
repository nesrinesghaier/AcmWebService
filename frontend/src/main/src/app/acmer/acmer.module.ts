import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';
import {AcmerRoutingModule} from './routing/acmer-routing.module';
import {AcmerListComponent} from './component/acmer-list/acmer-list.component';
import {AcmerAddComponent} from './component/acmer-add/acmer-add.component';
import {AcmerDetailsComponent} from './component/acmer-details/acmer-details.component';
import {FormsModule} from '@angular/forms';
import {SlimLoadingBarModule} from 'ng2-slim-loading-bar';
import {HttpClientModule} from '@angular/common/http';
import {AcmerEditComponent} from './component/acmer-edit/acmer-edit.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatTableModule} from '@angular/material/table';
import {MatPaginatorModule, MatSortModule} from '@angular/material';
import {DataTableComponent} from './component/data-table/data-table.component';
import {MatButtonModule} from '@angular/material/button';
import {NgxPaginationModule} from 'ngx-pagination';
import {DataTableModule} from "angular-6-datatable";

@NgModule({
  imports: [
    CommonModule,
    AcmerRoutingModule,
    FormsModule,
    HttpClientModule,
    SlimLoadingBarModule.forRoot(),
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatButtonModule,
    NgxPaginationModule,
    DataTableModule

  ],
  declarations: [AcmerListComponent, AcmerAddComponent, AcmerDetailsComponent, AcmerEditComponent, DataTableComponent]
})
export class AcmerModule {
}
