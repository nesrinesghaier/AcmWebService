import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "./service/authentication.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  mode: number = 0;
  public handle: string;
  public password: string;

  constructor(private authService: AuthenticationService, private router: Router) {
  }

  ngOnInit() {
    if ( localStorage.getItem('loggedIn')!=null){
      this.router.navigate(['acmers']);
    }
  }

  onLogin(handle, password) {
    console.log(handle);
    console.log(password);
    this.authService.login(handle, password).subscribe(resp => {
      let jwt = resp['token'];
      let username = resp['handle'];
      let role = resp['role'];
      console.log(jwt);
      console.log(JSON.stringify(resp));
      this.mode = 0;
      localStorage.setItem('token', jwt);
      localStorage.setItem('handle', username);
      localStorage.setItem('role', role.toString());
      localStorage.setItem('loggedIn', 'true');
      this.router.navigate(['acmers']);
      window.location.reload(true);
    }, error => {
      this.mode = 1;

    });

  }
}
