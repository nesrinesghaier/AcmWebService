import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AcmerService} from "../../service/acmer.service";
import {Acmer} from "../../model/Acmer";
import {RegisterService} from "../../../Authentication/service/register.service";

@Component({
  selector: 'app-acmer-edit',
  templateUrl: './acmer-edit.component.html',
  styleUrls: ['./acmer-edit.component.css']
})

export class AcmerEditComponent implements OnInit {
  public emailRegex = '^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$';
  public passwordRegex = '^[0-9]{8}$';
  public isError: boolean = false;
  public errorMessage: string = '';
  public hasTried: boolean = false;
  public acmer: Acmer = new Acmer();
  public handle: string = '';

  constructor(private router: Router, private acmerService: AcmerService, private route: ActivatedRoute, private registerService: RegisterService) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.handle = params.handle;
      if (sessionStorage.getItem('role') != 'ADMIN' && sessionStorage.getItem('handle') != this.handle) {
        this.router.navigate(['/acmers']);
      }
    });
    this.acmerService.getAcmerByHandle(this.handle).subscribe(acmer => {
      this.acmer = acmer;
      this.acmer.password = '';
    }, () => {
      this.router.navigate(['/acmers']);
    });
  }

  editAcmer(): void {
    this.hasTried = true;
    if (this.validateForm()) {
      this.acmerService.updateAcmer(this.acmer).subscribe(() => {
        alert("Acmer edited successfully");
        this.router.navigate(['/acmers']);
      }, error => {
        this.isError = true;
        if (error.status == 504) {
          this.errorMessage = 'No Connection!';
        }
      });
    }

  }

  validateForm(): boolean {
    return this.acmer.firstName != '' &&
      this.acmer.lastName != '' &&
      this.acmer.email != '' &&
      this.acmer.email.match(this.emailRegex) != null &&
      (this.acmer.password == '' ||
        this.acmer.password.match(this.passwordRegex) != null);
  }
}

