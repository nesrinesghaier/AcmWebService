import {Component, Input, OnInit, Output,EventEmitter} from '@angular/core';
import {ActivatedRoute, NavigationExtras, Router} from "@angular/router";
import {AcmerService} from "../../service/acmer.service";
import {Acmer} from "../../model/Acmer";
import {FormBuilder, FormGroup, Validators, FormControl,FormArray} from "@angular/forms";
import {first} from "rxjs/operators";

@Component({
  selector: 'app-acmer-edit',
  templateUrl: './acmer-edit.component.html',
  styleUrls: ['./acmer-edit.component.css']
})
export class AcmerEditComponent implements OnInit {
  option: FormGroup;
  items: any;
  empty:string="";
  @Output()
  eventEmitter: EventEmitter<string> = new EventEmitter<string>();

  acmer:Acmer = new Acmer();
  public handle: string;
  acmerHandle: string;
  acmerEmail = 'handle Email';
  acmerFirstName = 'acmerFirstName';
  acmerLastName = 'acmerLastName';

  constructor(private router: Router, private acmerService: AcmerService, private formBuilder: FormBuilder,private route: ActivatedRoute) {

    this.option = this.formBuilder.group({
      firstName: ['', Validators.required],
      obligation: '',
    });
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.handle = params.handle;
    });
    this.acmerService.getAcmerByHandle(this.handle).subscribe(acmer=>{
      console.log(JSON.stringify(acmer));
      this.acmer.handle = acmer['handle']==null?"":acmer['handle'];
      this.acmer.firstName = acmer['firstName']==null?"":acmer['firstName'];
      this.acmer.lastName= acmer['lastName']==null?"":acmer['lastName'];
      this.acmer.email = acmer['email']==null?"":acmer['email'];
      this.acmer.country = acmer['country']==null?"":acmer['country'] ;
      this.acmer.rank= acmer['rank']==null?"":acmer['rank'] ;
      this.acmer.maxRank= acmer['maxRank']==null?"":acmer['maxRank'] ;
      this.acmer.rating= acmer['rating']==null?"":acmer['rating'] ;
      this.acmer.maxRating= acmer['maxRating']==null?"":acmer['maxRating'] ;
      this.acmer.solvedProblems= acmer['solvedProblems']==null?"":acmer['solvedProblems'] ;
      this.acmer.score= acmer['score']==null?"":acmer['score'] ;
      this.acmer.password= acmer['password']==null?"":acmer['password'] ;
      this.acmer.role= acmer['role']==null?"":acmer['role'] ;
    },error=>{
     console.log(error);
   });
  }
  editAcmer(): void {
    this.acmerService.updateAcmer(this.acmer).subscribe(data => {
      this.router.navigate(['acmers']);
    }, error => console.log(error));
  }

}

