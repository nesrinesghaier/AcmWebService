import {Injectable} from '@angular/core';
import {User} from '../@core/data/users';
import {HttpClient, HttpHeaders,} from '@angular/common/http';


@Injectable()
export class AcmerService {

  public apiUrl = '/api/acmers';
  private reqHeader: HttpHeaders;
  private jwt: string;

  constructor(private http: HttpClient) {
    this.jwt = sessionStorage.getItem('token');
    this.reqHeader = new HttpHeaders({
      'Accept': 'application/json, text/plain, */*',
      'Content-Type': 'application/json',
      'Authorization': this.jwt
    });
  }

  getAllAcmers() {
    return this.http.get<Array<User>>(this.apiUrl, {headers: this.reqHeader});
  }

  getAcmerByHandle(handle: string) {
    return this.http.get<User>(this.apiUrl + '/' + handle, {headers: this.reqHeader});
  }

  createAcmer(acmer: User) {
    return this.http.post(this.apiUrl + '/create', acmer, {headers: this.reqHeader});
  }

  updateAcmer(acmer: User) {
    return this.http.put(this.apiUrl, acmer, {headers: this.reqHeader});
  }

  deleteAcmer(acmer: User) {
    return this.http.delete<Array<User>>(this.apiUrl + '/' + acmer.handle, {headers: this.reqHeader});
  }

  deleteAllAcmers() {
    return this.http.delete<Array<User>>(this.apiUrl + '/deleteAll', {headers: this.reqHeader});
  }
}
