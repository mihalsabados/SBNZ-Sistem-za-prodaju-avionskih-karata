import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginCredentials } from 'src/app/model/loginCredentials';


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

  getUserStatus(email: string){
    let headers = new HttpHeaders();
    return this.http.get<string>(this.backUrl + "get-status/"+ email, { headers, responseType: 'text' as 'json' });
  }
}
