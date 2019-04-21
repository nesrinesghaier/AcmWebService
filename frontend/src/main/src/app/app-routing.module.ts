import {ExtraOptions, RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {AuthGardService} from './services/auth-gard.service';

const routes: Routes = [
  {path: 'pages', canActivate: [AuthGardService], loadChildren: 'app/pages/pages.module#PagesModule'},
  {
    path: 'auth',
    loadChildren: 'app/module/auth/auth.module#AuthModule',
  },
  {path: '', redirectTo: 'pages', pathMatch: 'full'},
  {path: '**', redirectTo: 'pages'},
];

const config: ExtraOptions = {
  useHash: true,
};

@ NgModule({
  imports: [RouterModule.forRoot(routes, config)],
  exports: [RouterModule],
})
export class AppRoutingModule {
}
