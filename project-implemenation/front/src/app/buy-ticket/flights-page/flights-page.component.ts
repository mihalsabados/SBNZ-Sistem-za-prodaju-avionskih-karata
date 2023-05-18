import { FlightService } from './../../services/flight/flight.service';
import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-flights-page',
  templateUrl: './flights-page.component.html',
  styleUrls: ['./flights-page.component.scss']
})
export class FlightsPageComponent implements OnInit{
  displayedColumns = ['destination', 'distance', 'price', 'departure', 'soldTickets', 'numberOfSeats'];

  flights:any = [];

  dataSource = new MatTableDataSource(this.flights);

  constructor(private flightService:FlightService, private toaster:ToastrService, private router:Router) {}

  ngOnInit(): void {
    this.loadData();
  }

  private loadData(){
    this.flightService.getAllFlights()
    .subscribe({
      next: (response) => {
        this.flights = response;
        this.dataSource = new MatTableDataSource(this.flights);
      }
    }
  );
  }


  clickedRow(row:any){
    console.log(row);

  }

}
