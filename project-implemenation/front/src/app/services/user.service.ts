import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginCredentials } from '../model/loginCredentials';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class UserService {

  backUrl: string = 'http://localhost:8081/users/';

  constructor(private http: HttpClient) {}

  login(credentials: LoginCredentials): Observable<any> {
    console.log(this.backUrl + "login");
    return this.http.post(this.backUrl + "login", credentials);
  }
}
