import {Component, OnInit} from '@angular/core';
import {RegisterService} from "../service/register.service";
import {Router} from "@angular/router";
import {Acmer} from "../../acmer/model/Acmer";
import {applySourceSpanToExpressionIfNeeded} from "@angular/compiler/src/output/output_ast";


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  public emailRegex = '^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$';
  public passwordRegex = '^[0-9]{8}$';
  public tried: boolean = false;
  public error: boolean = false;
  public errorMessage: string = '';
  public acmer: Acmer = new Acmer();

  constructor(private registerService: RegisterService, private router: Router) {

  }

  ngOnInit() {
    if (sessionStorage.getItem('loggedIn') != null) {
      this.router.navigate(['acmers']);
    }
  }

  register() {
    this.tried = true;
    if (this.validateForm()) {
      this.registerService.checkHandle(this.acmer.handle).subscribe(data => {
        if (data['status'] == 'OK') {
          this.registerService.register(this.acmer).subscribe(data => {
            alert("You are registered successfully");
            this.router.navigate(['login']);
          }, e => {
            this.error = true;
            if (e.status == 504) {
              this.errorMessage = 'No Connection!';
            } else if (e.status == 409) {
              this.errorMessage = 'Handle already registered, Contact admins!';
            }
          });
        } else {
          this.error = true;
          this.errorMessage = 'This handle is not Valid!';
        }
      }, e => {
        this.error = true;
        this.errorMessage = 'This handle is not Valid!';
      });
    }
  }

  validateForm(): boolean {
    return this.acmer.handle != '' &&
      this.acmer.firstName != '' &&
      this.acmer.lastName != '' &&
      this.acmer.email != '' &&
      this.acmer.email.match(this.emailRegex) != null &&
      this.acmer.password != '' &&
      this.acmer.password.match(this.passwordRegex) != null;
  }

}
