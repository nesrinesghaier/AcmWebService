import {Component, OnInit} from '@angular/core';
import {Acmer} from "../../model/Acmer";
import {Router} from "@angular/router";
import {AcmerService} from "../../service/acmer.service";

import "rxjs-compat/add/operator/map";
import {$} from "protractor";
import {until} from "selenium-webdriver";
import alertIsPresent = until.alertIsPresent;

@Component({
  selector: 'app-acmer-add',
  templateUrl: './acmer-add.component.html',
  styleUrls: ['./acmer-add.component.css']
})
export class AcmerAddComponent {
  handle: string = "";
  firstName: string = "";
  lastName: string = "";
  email: string = "";
  password: string = "";

  constructor(private router: Router, private acmerService: AcmerService) {
  }

  createAcmer(): void {
    if (this.handle != "" && this.password.match('^[0-9]{8}$') )  {
      let postMessage = JSON.stringify({
        'handle': this.handle,
        'firstName':this.firstName,
        'lastName':this.firstName,
        'email':this.email,
        'password': this.password
      });
      this.acmerService.createAcmer(postMessage).subscribe(data => {
        alert("Acmer created successfully");
        this.router.navigate(['acmers']);
      });
    }


  };

  listRefresh():void{
    let postMessage = JSON.stringify({
      'handle': this.handle,
      'firstName':this.firstName,
      'lastName':this.firstName,
      'email':this.email,
      'password': this.password
    });
    this.acmerService.createAcmer(postMessage).subscribe(data => {
    });
    window.location.reload();
  }

}
