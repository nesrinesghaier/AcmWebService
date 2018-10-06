import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Acmer} from "../../acmer/model/Acmer";

@Injectable()
export class RegisterService {

  private apiUrl = '/auth';
  private reqHeader: HttpHeaders;

  constructor(private http: HttpClient) {
    this.reqHeader = new HttpHeaders({
      'Accept': 'application/json, text/plain, */*',
      'Content-Type': 'application/json',
    });
  }

  register(acmer: Acmer) {
    return this.http.post<Acmer>(this.apiUrl + '/register', acmer, {headers: this.reqHeader});
  }
}
