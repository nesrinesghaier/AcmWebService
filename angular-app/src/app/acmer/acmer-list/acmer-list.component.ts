import {Component, OnInit, ViewChild} from '@angular/core';
import {AcmerService} from "../acmer.service";
import {Acmer} from "../acmer";
import {Observable} from "rxjs/index";
import {Router} from "@angular/router";
import { FileUploader } from 'ng2-file-upload';
import {AngularFileUploaderComponent} from "angular-file-uploader";
import {RequestOptions} from "@angular/http";
import {Http, Response} from "@angular/http";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-acmer-list',
  templateUrl: './acmer-list.component.html',
  styleUrls: ['./acmer-list.component.css'],
  providers: [AcmerService]
})
export class AcmerListComponent implements OnInit {
  acmers: Acmer[];
  empty:string= "####";
  apiEndPoint ="http://localhost:8080/acmers/createAll";
  handle:string;


  constructor(private acmerService: AcmerService,private router: Router,private http: HttpClient) {
  }

  ngOnInit() {
    this.acmerService.getAllAcmers().subscribe(data => {
      this.acmers = data;
    });
  }

  getAllAcmers() {
    console.log('fetching');
    if(this.acmers) console.log('empty');
    this.acmerService.getAllAcmers().subscribe(data => {
      this.acmers = data;
    }, error => console.log(error));
  }

  deleteAcmer(acmer:Acmer){
    console.log(acmer.handle);
    this.acmerService.deleteAcmer(acmer).subscribe(data => {
      this.acmers = this.acmers.filter(a=> a!== acmer);
    }, error => console.log(error));
  }
  editAcmer(acmer:Acmer){

  }
  deleteAcmers() {
    this.acmerService.deleteAllAcmers().subscribe(data => {
      window.location.reload();
    }, error => console.log('ERROR: ' + error));

  };

  refreshPage() :void{
    this.router.navigate(['acmers']);
  };
  fileUpload(event) {
    let fileList: FileList = event.target.files;
    if(fileList.length > 0) {
      let file: File = fileList[0];
      let formData:FormData = new FormData();
      formData.append('uploadFile', file, file.name);
      let headers = new Headers();
      headers.append('Accept', 'application/json');
      this.http.post(this.apiEndPoint, formData).subscribe(
          error => console.log(error)
        );
      alert("upload done");
    }else {
      alert("please select a valid file");
    }
  }
}
