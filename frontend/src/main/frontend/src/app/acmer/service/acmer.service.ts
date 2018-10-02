import {Injectable} from '@angular/core';
import * as _ from 'lodash';
import {Headers} from "@angular/http";
import {Acmer} from "../model/Acmer";
import {Http, Response} from "@angular/http";
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {
  HttpClient, HttpErrorResponse, HttpEventType, HttpHeaders, HttpRequest,
  HttpResponse
} from "@angular/common/http";
import {catchError} from "rxjs/internal/operators";
import {Subscription} from 'rxjs/Subscription';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';


export enum FileQueueStatus {
  Pending,
  Success,
  Error,
  Progress
}

export class FileQueueObject {
  public file: any;
  public status: FileQueueStatus = FileQueueStatus.Pending;
  public progress: number = 0;
  public request: Subscription = null;
  public response: HttpResponse<any> | HttpErrorResponse = null;
  // actions
  public upload = () => { /* set in service */
  };
  public cancel = () => { /* set in service */
  };
  public remove = () => { /* set in service */
  };
  // statuses
  public isPending = () => this.status === FileQueueStatus.Pending;
  public isSuccess = () => this.status === FileQueueStatus.Success;
  public isError = () => this.status === FileQueueStatus.Error;
  public inProgress = () => this.status === FileQueueStatus.Progress;
  public isUploadable = () => this.status === FileQueueStatus.Pending || this.status === FileQueueStatus.Error;

  constructor(file: any) {
    this.file = file;
  }

}

@Injectable()
export class AcmerService {

  private apiUrl = '/api/acmers';
  private _files: FileQueueObject[] = [];
  private reqHeader = new HttpHeaders({
    'Content-Type': 'application/json',
    'No-Auth': 'True'
  });

  constructor(private http: HttpClient) {
    this._queue = <BehaviorSubject<FileQueueObject[]>>new BehaviorSubject(this._files);
  }

  private _queue: BehaviorSubject<FileQueueObject[]>;

  public get queue() {
    return this._queue.asObservable();
  }

  getAllAcmers() {
    let jwt = localStorage.getItem('token');
    return this.http.get<Acmer[]>(this.apiUrl, {headers : new HttpHeaders().set('X-Auth-Token', jwt)});
  }

  getAcmerByHandle(handle: string) {
    return this.http.get(this.apiUrl + '/' + handle);
  }

  createAcmer(json: string) {
    const httpOptions = {
      headers: new HttpHeaders({'Content-Type': 'application/json'})
    };
    return this.http.post<string>(this.apiUrl + '/create', json, httpOptions);
  }

  deleteAcmer(acmer: Acmer) {
    return this.http.delete(this.apiUrl + '/' + acmer.handle);
  }

  updateAcmer(acmer: Acmer): Observable<Object> {
    return this.http.put<Acmer>(this.apiUrl , acmer);
  }

  deleteAllAcmers() {
    return this.http.delete(this.apiUrl + '/deleteAll', {responseType: 'text'});
  }

  // public events
  public onCompleteItem(queueObj: FileQueueObject, response: any): any {
    return {queueObj, response};
  }

  // public functions
  public addToQueue(data: any) {
    // add file to the queue
    _.each(data, (file: any) => this._addToQueue(file));
  }

  public clearQueue() {
    // clear the queue
    this._files = [];
    this._queue.next(this._files);
  }

  public uploadAll() {
    // upload all except already successfull or in progress
    _.each(this._files, (queueObj: FileQueueObject) => {
      if (queueObj.isUploadable()) {
        this._upload(queueObj);
      }
    });
  }

  // private functions
  private _addToQueue(file: any) {
    const queueObj = new FileQueueObject(file);

    // set the individual object events
    queueObj.upload = () => this._upload(queueObj);
    queueObj.remove = () => this._removeFromQueue(queueObj);
    queueObj.cancel = () => this._cancel(queueObj);

    // push to the queue
    this._files.push(queueObj);
    this._queue.next(this._files);
  }

  private _removeFromQueue(queueObj: FileQueueObject) {
    _.remove(this._files, queueObj);
  }

  private _upload(queueObj: FileQueueObject) {
    // create form data for file
    const form = new FormData();
    form.append('file', queueObj.file, queueObj.file.name);

    // upload file and report progress
    const req = new HttpRequest('POST', this.apiUrl + '/createAll', form, {
      reportProgress: true,
    });

    // upload file and report progress
    queueObj.request = this.http.request(req).subscribe(
      (event: any) => {
        if (event.type === HttpEventType.UploadProgress) {
          this._uploadProgress(queueObj, event);
        } else if (event instanceof HttpResponse) {
          this._uploadComplete(queueObj, event);
        }
      },
      (err: HttpErrorResponse) => {
        if (err.error instanceof Error) {
          // A client-side or network error occurred. Handle it accordingly.
          this._uploadFailed(queueObj, err);
        } else {
          // The backend returned an unsuccessful response code.
          this._uploadFailed(queueObj, err);
        }
      }
    );

    return queueObj;
  }

  private _cancel(queueObj: FileQueueObject) {
    // update the FileQueueObject as cancelled
    queueObj.request.unsubscribe();
    queueObj.progress = 0;
    queueObj.status = FileQueueStatus.Pending;
    this._queue.next(this._files);
  }

  private _uploadProgress(queueObj: FileQueueObject, event: any) {
    // update the FileQueueObject with the current progress
    const progress = Math.round(100 * event.loaded / event.total);
    queueObj.progress = progress;
    queueObj.status = FileQueueStatus.Progress;
    this._queue.next(this._files);
  }

  private _uploadComplete(queueObj: FileQueueObject, response: HttpResponse<any>) {
    // update the FileQueueObject as completed
    queueObj.progress = 100;
    queueObj.status = FileQueueStatus.Success;
    queueObj.response = response;
    this._queue.next(this._files);
    this.onCompleteItem(queueObj, response.body);
  }

  private _uploadFailed(queueObj: FileQueueObject, response: HttpErrorResponse) {
    // update the FileQueueObject as errored
    queueObj.progress = 0;
    queueObj.status = FileQueueStatus.Error;
    queueObj.response = response;
    this._queue.next(this._files);
  }


}
