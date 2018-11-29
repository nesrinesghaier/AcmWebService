import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../service/authentication.service";
import {Router} from "@angular/router";
import {Acmer} from "../../acmer/model/Acmer";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  public error: boolean = false;
  public errorMessage: string = '';
  public acmer: Acmer = new Acmer();

  constructor(private authService: AuthenticationService, private router: Router) {
  }

  ngOnInit() {
    if (sessionStorage.getItem('loggedIn') != null) {
      this.router.navigate(['acmers']);
    }
  }

  onLogin() {
    this.error = false;
    this.authService.login(this.acmer.handle, this.acmer.password).subscribe(resp => {
      let jwt = resp['token'];
      let username = resp['handle'];
      let role = resp['role'];
      sessionStorage.setItem('token', 'Bearer ' + jwt);
      sessionStorage.setItem('handle', username);
      sessionStorage.setItem('role', role.toString());
      sessionStorage.setItem('loggedIn', 'true');
      this.router.navigate(['acmers']);
      AuthenticationService.loggedIn = sessionStorage.getItem('loggedIn') != null;
      AuthenticationService.adminPrevilege = sessionStorage.getItem('role') == "ADMIN";
      AuthenticationService.handle = (sessionStorage.getItem('handle') == null) ? "" : sessionStorage.getItem('handle');
    }, e => {
      this.error = true;
      if (e.status == 504) {
        this.errorMessage = 'No Connection!';
      } else {
        this.errorMessage = 'Wrong Credentials';
      }
    });
  }
}
