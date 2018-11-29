import {Injectable} from '@angular/core';
import * as _ from 'lodash';
import {Acmer} from "../model/Acmer";
import {Observable} from 'rxjs';
import {
  HttpClient,
  HttpErrorResponse,
  HttpEventType,
  HttpHeaders,
  HttpRequest,
  HttpResponse
} from "@angular/common/http";
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
  private reqHeader: HttpHeaders;
  private jwt: string;

  constructor(private http: HttpClient) {
    this._queue = <BehaviorSubject<FileQueueObject[]>>new BehaviorSubject(this._files);
    this.jwt = sessionStorage.getItem('token');
    this.reqHeader = new HttpHeaders({
      'Accept': 'application/json, text/plain, */*',
      'Content-Type': 'application/json',
      'Authorization': this.jwt
    });
  }

  private _queue: BehaviorSubject<FileQueueObject[]>;

  public get queue() {
    return this._queue.asObservable();
  }

  getAllAcmers() {
    return this.http.get<Array<Acmer>>(this.apiUrl, {headers: this.reqHeader});
  }

  getAcmerByHandle(handle: string) {
    return this.http.get<Acmer>(this.apiUrl + '/' + handle, {headers: this.reqHeader});
  }

  createAcmer(acmer: Acmer) {
    return this.http.post(this.apiUrl + '/create', acmer, {headers: this.reqHeader});
  }

  updateAcmer(acmer: Acmer) {
    return this.http.put(this.apiUrl, acmer, {headers: this.reqHeader});
  }

  deleteAcmer(acmer: Acmer) {
    return this.http.delete<Array<Acmer>>(this.apiUrl + '/' + acmer.handle, {headers: this.reqHeader});
  }

  deleteAllAcmers() {
    return this.http.delete<Array<Acmer>>(this.apiUrl + '/deleteAll', {headers: this.reqHeader});
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
      headers: new HttpHeaders({
        'Authorization': sessionStorage.getItem('token')
      })
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
    queueObj.progress = Math.round(100 * event.loaded / event.total);
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
