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
        this.report.totalAmount = new Intl.NumberFormat().format(this.report.totalAmount);
        this.report.averagePrice = new Intl.NumberFormat().format(this.report.averagePrice.toFixed());
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

    this.ticketService.getTicketsReport(data)
    .subscribe({
      next: (response:any) => {
        console.log(response);
        this.report = response;
        this.report.totalAmount = new Intl.NumberFormat().format(this.report.totalAmount);
        this.report.averagePrice = new Intl.NumberFormat().format(this.report.averagePrice.toFixed());
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
                data: [this.report.numberOfBusinessTickets, this.report.numberOfEconomicTickets],
                backgroundColor: ['rgba(255, 159, 64, 0.8)', 'rgba(75, 192, 192, 0.8)'],
                hoverBackgroundColor: ['rgba(255, 159, 64, 0.9)', 'rgba(75, 192, 192, 0.9)']
            }
        ]
    };
    
    this.avgOccuChartData = {
        labels: ['Occupied', 'Empty'],
        datasets: [
            {
                data: [this.report.averageOccupancy*100, 100-this.report.averageOccupancy*100],
                backgroundColor: ['rgba(255, 159, 64, 0.8)', 'rgba(75, 192, 192, 0.8)'],
                hoverBackgroundColor: ['rgba(255, 159, 64, 0.9)', 'rgba(75, 192, 192, 0.9)']
            }
        ]
    };

    this.chartOptions = {
      cutout: '60%',
      plugins: {
          legend: {
              labels: {
                  color: 'black',
                  useBorderRadius: true,
                  borderRadius: 50
              }
          }
      }
  };
  }

}
