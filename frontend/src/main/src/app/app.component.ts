import {Component, OnInit} from '@angular/core';
import {SlimLoadingBarService} from 'ng2-slim-loading-bar';
import {
  NavigationCancel,
  Event,
  NavigationEnd,
  NavigationError,
  NavigationStart,
  Router
} from '@angular/router';

import {AuthenticationService} from "./Authentication/service/authentication.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  public authService = AuthenticationService;

  constructor(private loadingBar: SlimLoadingBarService, private router: Router) {

  }

  ngOnInit(): void {
    AuthenticationService.loggedIn = sessionStorage.getItem('loggedIn') != null;
    AuthenticationService.adminPrevilege = sessionStorage.getItem('role') == "ADMIN";
    AuthenticationService.handle = (sessionStorage.getItem('handle') == null) ? "" : sessionStorage.getItem('handle');
    this.router.events.subscribe((event: Event) => {
      this.navigationInterceptor(event);
    });
  }

  onLogout() {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('handle');
    sessionStorage.removeItem('loggedIn');
    sessionStorage.removeItem('role');
    this.authService.loggedIn = false;
    this.authService.adminPrevilege = false;
    this.authService.handle = null;
  }

  private navigationInterceptor(event: Event): void {
    if (event instanceof NavigationStart) {
      this.loadingBar.start();
    }
    if (event instanceof NavigationEnd) {
      this.loadingBar.complete();
    }
    if (event instanceof NavigationCancel) {
      this.loadingBar.stop();
    }
    if (event instanceof NavigationError) {
      this.loadingBar.stop();
    }
  }
}
