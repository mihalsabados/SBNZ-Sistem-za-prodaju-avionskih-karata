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

  ticketChartData: any;
  avgOccuChartData: any;
  chartOptions:any;
  report:any;
  
  sortVariable:any = "";
  ascOrder:boolean = true;

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

    this.fillChart();
  }



  loadData(){
    this.ticketService.getAllTickets()
    .subscribe({
      next: (response:any) => {
        console.log(response);
        this.report = response;
        this.report.totalAmount = new Intl.NumberFormat('en-DE').format(this.report.totalAmount);
        this.report.averagePrice = new Intl.NumberFormat('en-DE').format(this.report.averagePrice.toFixed());
        this.tickets = response.tickets;
        this.dataSource = new MatTableDataSource(this.tickets);
        this.fillChart();
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
    this.sortVariable = "";

    this.ticketService.getTicketsReport(data)
    .subscribe({
      next: (response:any) => {
        console.log(response);
        this.report = response;
        this.report.totalAmount = new Intl.NumberFormat('en-DE').format(this.report.totalAmount);
        this.report.averagePrice = new Intl.NumberFormat('en-DE').format(this.report.averagePrice.toFixed());
        this.tickets = response.tickets;
        this.dataSource = new MatTableDataSource(this.tickets);
        this.fillChart();
      }
    }
  );
  }

  fillChart(){
    this.ticketChartData = {
        labels: ['Business Tickets', 'Economic Tickets'],
        datasets: [
            {
                data: [this.report?.numberOfBusinessTickets, this.report?.numberOfEconomicTickets],
                backgroundColor: ['#3250a8', '#a3aabf'],
                hoverBackgroundColor: ['#3250a8', '#a3aaba']
            }
        ]
    };
    
    this.avgOccuChartData = {
        labels: ['Occupied', 'Empty'],
        datasets: [
            {
                data: [this.report?.averageOccupancy*100, 100-this.report?.averageOccupancy*100],
                backgroundColor: ['#3250a8', '#a3aabf'],
                hoverBackgroundColor: ['#3250a8', '#a3aabf']
            }
        ]
    };
  }

  setSortVariable(event:any, variable:any){
    event.preventDefault();
    event.stopPropagation();
    if(this.sortVariable == variable)
      this.ascOrder = !this.ascOrder
    else{
      this.ascOrder = true;
      this.sortVariable = variable;
    }

    let data = {
      ticketsReportTemplate:{
        ...this.filterData.value,
        ...this.range.value,
      },
      sortTemplate:{
        sortParameter: this.sortVariable,
        ascOrder: this.ascOrder
      }
    }

    this.ticketService.sortTickets(data).subscribe({
        next: (response:any) => {
          console.log(response);
          this.tickets = response;
          this.dataSource = new MatTableDataSource(this.tickets);
        }
    });

  }

}
