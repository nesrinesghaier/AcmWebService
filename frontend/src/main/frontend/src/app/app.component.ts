import {Component, ViewChild} from '@angular/core';
import {SlimLoadingBarService} from 'ng2-slim-loading-bar';

import {
  NavigationCancel,
  Event,
  NavigationEnd,
  NavigationError,
  NavigationStart,
  Router
} from '@angular/router';
import {AcmerService} from "./acmer/service/acmer.service";
import {AuthenticationService} from "./login/service/authentication.service";
import {LocaleDataIndex} from "@angular/common/src/i18n/locale_data";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  loggedIn = false;
  adminPrevilege = false;
  handle = "";
  title = 'frontend';

  constructor(private _loadingBar: SlimLoadingBarService, private _router: Router, public acmerService: AuthenticationService) {
    this.loggedIn = localStorage.getItem('loggedIn') != null;
    console.log(localStorage.getItem('role'));
    this.adminPrevilege = localStorage.getItem('role') == "ADMIN";
    this.handle = (localStorage.getItem('handle') == null) ? "" : localStorage.getItem('handle');
    console.log(this.loggedIn);
    this._router.events.subscribe((event: Event) => {
      this.navigationInterceptor(event);
    });
  }

  onLogout() {
    localStorage.removeItem('token');
    localStorage.removeItem('handle');
    localStorage.removeItem('loggedIn');
    localStorage.removeItem('role');
    this.loggedIn = false;
    this.adminPrevilege = false;
    this.handle = null;
  }

  private navigationInterceptor(event: Event): void {
    if (event instanceof NavigationStart) {
      this._loadingBar.start();
    }
    if (event instanceof NavigationEnd) {
      this._loadingBar.complete();
    }
    if (event instanceof NavigationCancel) {
      this._loadingBar.stop();
    }
    if (event instanceof NavigationError) {
      this._loadingBar.stop();
    }
  }
}
