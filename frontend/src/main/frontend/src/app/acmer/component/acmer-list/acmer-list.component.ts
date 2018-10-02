
import {Component, OnInit, EventEmitter, Output, ViewChild, Input} from '@angular/core';
import {first} from "rxjs/operators";
import {FileQueueObject,AcmerService} from "../../service/acmer.service";
import {Acmer} from "../../model/Acmer";import {ActivatedRoute, Router} from "@angular/router";
import { DataSource } from '@angular/cdk/collections';
import { MatPaginator, MatSort } from '@angular/material';
import { map } from 'rxjs/operators';
import { Observable, of as observableOf, merge } from 'rxjs';

@Component({
  selector: 'app-acmer-list',
  templateUrl: './acmer-list.component.html',
  styleUrls: ['./acmer-list.component.css'],
  providers: [AcmerService]
})



export class AcmerListComponent implements OnInit  {

  acmers: Acmer[];
  selectedAcmer: Acmer;
  empty: string = "";
  firstName: string;
  lastName: string;
  email: string;
  country: string;
  loggedInAcmer:string;
  adminPrevilege=false;

  @Output() onCompleteItem = new EventEmitter();
  @Input() acmer:Acmer;
  @ViewChild('fileInput') fileInput;
  queue: Observable<FileQueueObject[]>;
  completeItem = (item: FileQueueObject, response: any) => {
    this.onCompleteItem.emit({item, response});
  }

  constructor(private acmerService: AcmerService, private router: Router, private route: ActivatedRoute) {
    this.loggedInAcmer = (localStorage.getItem('handle') == null) ? "" : localStorage.getItem('handle');
    this.adminPrevilege = localStorage.getItem('role') == "ADMIN";
  }

  deleteAcmer(acmer: Acmer) {
    this.acmerService.deleteAcmer(acmer).subscribe(data => {
      this.acmers = this.acmers.filter(a => a !== acmer);
    }, error => console.log(error));
  }



  deleteAcmers() {
    this.acmerService.deleteAllAcmers().subscribe(data => {
      window.location.reload();
    }, error => console.log('ERROR: ' + error));

  };

  refreshPage(): void {
    window.location.reload();
  };

  ngOnInit() {
    console.log(this.loggedInAcmer);
    this.acmerService.getAllAcmers().subscribe(data => {
      console.log(data);
      this.acmers = data;
    }, error => console.log(error));
    this.queue = this.acmerService.queue;
    this.acmerService.onCompleteItem = this.completeItem;
  }

  addToQueue() {
    const fileBrowser = this.fileInput.nativeElement;
    this.acmerService.addToQueue(fileBrowser.files);
  }
}
