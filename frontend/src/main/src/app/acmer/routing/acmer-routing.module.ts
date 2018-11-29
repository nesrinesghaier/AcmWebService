import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {AcmerListComponent} from "../component/acmer-list/acmer-list.component";
import {AcmerAddComponent} from "../component/acmer-add/acmer-add.component";
import {AcmerEditComponent} from "../component/acmer-edit/acmer-edit.component";
import {DataTableComponent} from "../component/data-table/data-table.component";
import {AcmerDetailsComponent} from "../component/acmer-details/acmer-details.component";

const routes: Routes = [
  {path: 'acmers', component: AcmerListComponent},
  {path: 'acmers/create', component: AcmerAddComponent},
  //{path: 'acmers/login', component: DataTableComponent},
  {path: 'acmers/edit', redirectTo: 'acmers'},
  {path: 'acmers/edit/:handle', component: AcmerEditComponent},
  {path: 'acmers/details', redirectTo: 'acmers'},
  {path: 'acmers/details/:handle', component: AcmerDetailsComponent}
];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AcmerRoutingModule { }
