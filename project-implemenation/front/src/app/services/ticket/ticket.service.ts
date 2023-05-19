import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TicketData } from 'src/app/model/ticketData';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  backUrl: string = 'http://localhost:8081/ticket/';
  
  constructor(private http: HttpClient) { }
  
  createTicket(ticketData: TicketData): Observable<any>{
    return this.http.post(this.backUrl + "create-ticket", ticketData);
  }
}
