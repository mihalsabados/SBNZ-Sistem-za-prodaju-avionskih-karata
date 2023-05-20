import { FlightService } from './../../services/flight/flight.service';
import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/services/auth/auth.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-flights-page',
  templateUrl: './flights-page.component.html',
  styleUrls: ['./flights-page.component.scss']
})
export class FlightsPageComponent implements OnInit{
  displayedColumns = ['destination', 'distance', 'price', 'departure', 'soldTickets', 'numberOfSeats', 'popular'];

  flights:any = [];
  loggedUser:any;
  loyaltyColor: string;

  dataSource = new MatTableDataSource(this.flights);

  constructor(private flightService:FlightService, private toaster:ToastrService, private router:Router, private authService:AuthService) {}

  ngOnInit(): void {
    this.loadData();
    this.loggedUser = this.authService.getCurrentUser();
    this.loyaltyColor = this.loggedUser.loyaltyStatus == "REGULAR"?"#F0F8FF":this.loggedUser.loyaltyStatus == "BRONZE"?"#CD7F32":this.loggedUser.loyaltyStatus == "SILVER"?"#C0C0C0":"#FFD700";
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
    this.router.navigateByUrl("buy-ticket/"+ row.id + "/" + row.destination + "/" + row.departure);
  }

  logOut(){
    this.authService.logOut();
    this.router.navigateByUrl("/");
  }

}
