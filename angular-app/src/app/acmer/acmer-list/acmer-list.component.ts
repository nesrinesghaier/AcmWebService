
import {Component, OnInit, EventEmitter, Output, ViewChild, Input} from '@angular/core';
import {FileQueueObject, AcmerService} from './../acmer.service';
import {first} from "rxjs/operators";
import {Acmer} from "../acmer";
import {Observable} from "rxjs/index";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-acmer-list',
  templateUrl: './acmer-list.component.html',
  styleUrls: ['./acmer-list.component.css'],
  providers: [AcmerService]
})
export class AcmerListComponent implements OnInit {
  acmers: Acmer[];
  selectedAcmer: Acmer;
  empty: string = "####";
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

  getAllAcmers() {
    console.log('fetching');
    if (this.acmers) console.log('empty');
    this.acmerService.getAllAcmers().subscribe(data => {
      this.acmers = data;
    }, error => console.log(error));
  }

  deleteAcmer(acmer: Acmer) {
    console.log(acmer.handle);
    this.acmerService.deleteAcmer(acmer).subscribe(data => {
      this.acmers = this.acmers.filter(a => a !== acmer);
    }, error => console.log(error));
  }

  editAcmer(acmer: Acmer) {

    localStorage.removeItem("acmerHandle");
    localStorage.setItem("acmerHandle", acmer.handle);
    localStorage.setItem("acmerEmail", acmer.email);
    localStorage.setItem("acmerFirstName", acmer.firstName);
    localStorage.setItem("acmerLastName", acmer.lastName);
    localStorage.setItem("acmerCountry", acmer.country);
    this.router.navigate(['/acmers/edit',acmer.handle]);
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
    });
    this.queue = this.acmerService.queue;
    this.acmerService.onCompleteItem = this.completeItem;
  }

  addToQueue() {
    const fileBrowser = this.fileInput.nativeElement;
    this.acmerService.addToQueue(fileBrowser.files);
  }
}
