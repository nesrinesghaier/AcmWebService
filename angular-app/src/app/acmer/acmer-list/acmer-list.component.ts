
import {Component, OnInit, EventEmitter, Output, ViewChild, Input} from '@angular/core';
import {FileQueueObject, AcmerService} from './../acmer.service';
import {first} from "rxjs/operators";
import {Acmer} from "../acmer";
import {ActivatedRoute, Router} from "@angular/router";
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
  apiEndPoint = "http://localhost:8080/acmers/createAll";
  handle: string;

  @Output() onCompleteItem = new EventEmitter();
  @Input() acmer:Acmer;
  @ViewChild('fileInput') fileInput;
  queue: Observable<FileQueueObject[]>;
  completeItem = (item: FileQueueObject, response: any) => {
    this.onCompleteItem.emit({item, response});

  }

  constructor(private acmerService: AcmerService, private router: Router, private route: ActivatedRoute) {
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
    this.acmerService.getAllAcmers().subscribe(data => {
      this.acmers = data;
    }, error => console.log(error));
    //this.dataSource = new AcmersListDataSource(this.paginator, this.sort,this.acmerService);
    //this.dataSource.loadAcmersData(this.acmerService);
    this.queue = this.acmerService.queue;
    this.acmerService.onCompleteItem = this.completeItem;
  }

  addToQueue() {
    const fileBrowser = this.fileInput.nativeElement;
    this.acmerService.addToQueue(fileBrowser.files);
  }
}
