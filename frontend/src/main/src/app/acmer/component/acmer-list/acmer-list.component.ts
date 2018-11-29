import {Component, OnInit, EventEmitter, Output, ViewChild, OnDestroy} from '@angular/core';
import {FileQueueObject, AcmerService} from "../../service/acmer.service";
import {Acmer} from "../../model/Acmer";
import {ActivatedRoute, Router} from "@angular/router";
import {Observable, Subscription} from 'rxjs/Rx';

@Component({
  selector: 'app-acmer-list',
  templateUrl: './acmer-list.component.html',
  styleUrls: ['./acmer-list.component.css'],
  providers: [AcmerService]
})


export class AcmerListComponent implements OnInit, OnDestroy {

  acmers: Acmer[];
  empty: string = "";
  firstName: string;
  lastName: string;
  email: string;
  country: string;
  loggedInAcmer: string;
  adminPrevilege = false;
  private timer: Observable<number> = null;
  private subscription: Subscription = null;

  @Output() onCompleteItem = new EventEmitter();
  @ViewChild('fileInput') fileInput;
  queue: Observable<FileQueueObject[]>;
  completeItem = (item: FileQueueObject, response: any) => {
    this.onCompleteItem.emit({item, response});
  };

  constructor(private acmerService: AcmerService, private router: Router, private route: ActivatedRoute) {

  }

  ngOnInit() {
    if (sessionStorage.getItem('handle') == null) {
      this.router.navigate(['login']);
      return;
    }
    this.loggedInAcmer = sessionStorage.getItem('handle');
    this.adminPrevilege = sessionStorage.getItem('role') == "ADMIN";
    this.refreshData();
    this.queue = this.acmerService.queue;
    this.acmerService.onCompleteItem = this.completeItem;
    this.timer = Observable.interval(10000);
    this.subscription = this.timer.subscribe(x => {
      this.refreshData();
    });
  }

  refreshData(): void {
    this.acmerService.getAllAcmers().subscribe(data => {
      this.acmers = data.sort((a: Acmer, b: Acmer) => b.score - a.score);
    }, error => console.log(error));
  }

  deleteAcmer(acmer: Acmer) {
    this.acmerService.deleteAcmer(acmer).subscribe(data => {
      this.acmers = data.sort((a: Acmer, b: Acmer) => b.score - a.score);
    }, error => console.log(error));
  }

  deleteAcmers() {
    this.acmerService.deleteAllAcmers().subscribe(data => {
      window.location.reload();
    }, error => console.log('ERROR: ' + error));

  };

  addToQueue() {
    const fileBrowser = this.fileInput.nativeElement;
    this.acmerService.addToQueue(fileBrowser.files);
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
