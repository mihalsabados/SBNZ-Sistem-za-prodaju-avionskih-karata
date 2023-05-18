import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FlightService {

  backUrl: string = 'http://localhost:8081/flight/';
  
  constructor(private http: HttpClient) { }
  
  getAllFlights(): Observable<any>{
    return this.http.get(this.backUrl);
  }
}
