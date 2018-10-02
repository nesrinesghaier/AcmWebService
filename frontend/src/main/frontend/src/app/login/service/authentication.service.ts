import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Acmer} from "../../acmer/model/Acmer";

@Injectable()
export class AuthenticationService {
  authenticated = false;
  private hostUrl: string = "/api";
  private options = {headers: new HttpHeaders().set('Content-Type', 'application/json')};

  constructor(private http: HttpClient) {
  }

  login(handle: string, password: string) {
    return this.http.post<Acmer>(this.hostUrl + "/login", {'handle': handle, 'password': password}, this.options);
  }

  authenticate(credentials, callback) {

    const headers = new HttpHeaders(credentials ? {
      authorization: 'Basic ' + btoa(credentials.username + ':' + credentials.password)
    } : {});

    this.http.get('api/login', {headers: headers}).subscribe(response => {
      if (response['handle']) {
        this.authenticated = true;
      } else {
        this.authenticated = false;
      }
      return callback && callback();
    });

  }
}
