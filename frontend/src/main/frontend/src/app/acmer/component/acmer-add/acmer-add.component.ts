import {Component, OnInit} from '@angular/core';
import {Acmer} from "../../model/Acmer";
import {Router} from "@angular/router";
import {AcmerService} from "../../service/acmer.service";

import "rxjs-compat/add/operator/map";
import {RegisterService} from "../../../Authentication/service/register.service";

@Component({
  selector: 'app-acmer-add',
  templateUrl: './acmer-add.component.html',
  styleUrls: ['./acmer-add.component.css']
})
export class AcmerAddComponent implements OnInit {
  public emailRegex = '^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$';
  public passwordRegex = '^[0-9]{8}$';
  public error: boolean = false;
  public errorMessage: string = '';
  public tried: boolean = false;
  public acmer: Acmer = new Acmer();


  constructor(private router: Router, private acmerService: AcmerService, private registerService: RegisterService) {
  }

  ngOnInit(): void {
    if (sessionStorage.getItem('role') != "ADMIN") {
      this.router.navigate(['acmers']);
    }
  }

  createAcmer(): void {
    this.tried = true;
    if (this.validateForm()) {
      this.registerService.checkHandle(this.acmer.handle).subscribe(data => {
        if (data['status'] == 'OK') {
          this.registerService.register(this.acmer).subscribe(data => {
            alert("Acmer created successfully");
            this.router.navigate(['acmers']);
          }, e => {
            this.error = true;
            if (e.status == 504) {
              this.errorMessage = 'No Connection!';
            } else if (e.status == 409) {
              this.errorMessage = 'Handle already exists';
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
