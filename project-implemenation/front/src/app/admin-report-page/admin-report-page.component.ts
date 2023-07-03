import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { AuthService } from '../services/auth/auth.service';
import { TicketService } from '../services/ticket/ticket.service';

@Component({
  selector: 'app-admin-report-page',
  templateUrl: './admin-report-page.component.html',
  styleUrls: ['./admin-report-page.component.scss']
})
export class AdminReportPageComponent implements OnInit {

  displayedColumns = ['flightNo','destination', 'departure', 'passengerEmail','payerEmail', 'basePrice', 'finalPrice', 'ticketType', 'timestamp'];
  
  tickets:any = [];
  loggedUser:any;
  
  dataSource = new MatTableDataSource(this.tickets);

  destinations:any = [];

  destinationAuto:any;
  selectedValue:any;
  ticketTypes: string[] = ["Business", "Economic"];

  filterData: FormGroup;
  range:FormGroup;
  
  constructor(private ticketService:TicketService, private authService:AuthService){
  }
  
  ngOnInit(): void {
    this.loggedUser = this.authService.getCurrentUser();
    this.loadData();
    this.filterData = new FormGroup({
        destination: new FormControl('', []),
    })

    this.range = new FormGroup({
      startDeparture: new FormControl<Date | null>(null),
      endDeparture: new FormControl<Date | null>(null),
    });
  }

  loadData(){
    this.ticketService.getAllTickets()
    .subscribe({
      next: (response:any) => {
        this.tickets = response;
        this.dataSource = new MatTableDataSource(response);
      }
    }
  );
  }

  onFilter(){
    console.log(this.filterData.value);
    console.log(this.range.value);
    let data = {
      ...this.filterData.value,
      ...this.range.value,
    }
    console.log(data);

    this.ticketService.getTicketsReport(data)
    .subscribe({
      next: (response:any) => {
        console.log(response);
        // this.tickets = response;
        // this.dataSource = new MatTableDataSource(response);
      }
    }
  );
  }

}
