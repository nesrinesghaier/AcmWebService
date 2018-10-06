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
  public acmer: Acmer = new Acmer();

  constructor(private router: Router, private acmerService: AcmerService) {
    console.log(localStorage.getItem('role'));
    if (localStorage.getItem('role') != "ADMIN") {
      router.navigate(['/acmers']);
    }
  }

  createAcmer(): void {
    this.acmerService.createAcmer(this.acmer).subscribe(data => {
      alert("Acmer created successfully");
      this.router.navigate(['acmers']);
    });
  }

}
