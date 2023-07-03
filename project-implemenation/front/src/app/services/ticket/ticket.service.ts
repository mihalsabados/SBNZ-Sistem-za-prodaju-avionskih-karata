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

  acceptSuggestedFlight(ticketData: TicketData): Observable<any>{
    return this.http.post(this.backUrl + "accept-suggested-flight", ticketData);
  }

  getPriceTemplate(): Observable<any> {
    return this.http.get(this.backUrl + "get-price-template");
  }

  setPriceTemplates(data: any): Observable<any> {
    return this.http.post(this.backUrl + "set-price-template", data);
  }

  getAllTickets() {
    return this.http.get(this.backUrl);
  }

  getTicketsForUser(email: any) {
    return this.http.get(this.backUrl+email);
  }

  getTicketsReport(data: any) {
    return this.http.post(this.backUrl + "tickets-report", data);
  }
}
