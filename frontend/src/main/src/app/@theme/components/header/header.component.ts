import {Component, Input, OnInit} from '@angular/core';

import {NbMenuService, NbSidebarService} from '@nebular/theme';
import {UserData} from '../../../@core/data/users';
import {AnalyticsService, LayoutService} from '../../../@core/utils';
import {NbAuthJWTToken, NbAuthService, NbTokenService} from '@nebular/auth';
import {NbAccessChecker} from '@nebular/security';

@Component({
  selector: 'ngx-header',
  styleUrls: ['./header.component.scss'],
  templateUrl: './header.component.html',
})
export class HeaderComponent implements OnInit {

  @Input() position = 'normal';

  user = {};

  userMenu = [{title: 'Profile', icon: 'fa fa-user'}, {
    title: 'Log out',
    icon: 'fas fa-sign-out-alt',
    link: '/auth/login'
  }];

  constructor(private sidebarService: NbSidebarService,
              private menuService: NbMenuService,
              private userService: UserData,
              private analyticsService: AnalyticsService,
              private layoutService: LayoutService,
              private authService: NbAuthService,
              private tokenService: NbTokenService,
              public accessChecker: NbAccessChecker) {
    this.authService.getToken().subscribe(data => {
    });
    this.authService.onTokenChange()
      .subscribe((token: NbAuthJWTToken) => {
        if (token.isValid()) {
          this.user = token.getPayload()['sub'];
        }
      });
    this.menuService.onItemClick().subscribe((event) => {
      this.authService.logout('email').subscribe((event) => {
        this.tokenService.clear();
      });
    })
  }

  ngOnInit() {
    this.userService.getUsers()
      .subscribe((users: any) => this.user = users.nick);
  }

  toggleSidebar(): boolean {
    this.sidebarService.toggle(true, 'menu-sidebar');
    this.layoutService.changeLayoutSize();

    return false;
  }

  goToHome() {
    this.menuService.navigateHome();
  }

  startSearch() {
    this.analyticsService.trackEvent('startSearch');
  }
}
