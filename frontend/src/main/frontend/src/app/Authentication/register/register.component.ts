import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {Component, OnInit} from '@angular/core';
import {RegisterService} from "../service/register.service";
import {Router} from "@angular/router";
import {Acmer} from "../../acmer/model/Acmer";


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  public error: boolean = false;
  public acmer: Acmer = new Acmer();
  form: FormGroup;

  constructor(private registerService: RegisterService, private router: Router, private formBuilder: FormBuilder) {
    this.form = formBuilder.group({
      password: [''],
      confirmPassword: ['']
    })
  }

  ngOnInit() {
    if (localStorage.getItem('loggedIn') != null) {
      this.router.navigate(['acmers']);
    }
  }

  register() {
    this.registerService.register(this.acmer).subscribe(data => {
      alert("You are registered successfully");
      this.router.navigate(['login']);
    }, error1 => {
      this.error = true;
    });
  }

}
