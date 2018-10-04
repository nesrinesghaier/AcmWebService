import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Acmer} from "../../acmer/model/Acmer";

@Injectable()
export class AuthenticationService {
  authenticated = false;
  private hostUrl: string = "/auth";
  private options = {headers: new HttpHeaders().set('Content-Type', 'application/json')};

  constructor(private http: HttpClient) {
  }

  login(handle: string, password: string) {
    let acmer = new Acmer();
    acmer.handle = handle;
    acmer.password = password;
    return this.http.post<Acmer>(this.hostUrl + "/login", acmer, this.options);
  }
}
