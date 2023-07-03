import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user/user.service';
import { AuthService } from '../services/auth/auth.service';
import { ToastrService } from 'ngx-toastr';
import { FlightService } from '../services/flight/flight.service';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit{

  loggedUser:any;
  userStatus:string;
  loyaltyColor: string;

  constructor(private flightService:FlightService, private toaster:ToastrService, private router:Router, private authService:AuthService, private userService:UserService) {}

  ngOnInit(): void {
    this.loggedUser = this.authService.getCurrentUser();
    this.getUserStatus();
  }

  private getUserStatus() {
    this.userService.getUserStatus(this.loggedUser.email)
    .pipe(catchError(err => {return throwError(() => {new Error('greska')} )}))
    .subscribe({
      next: (response:string) => {
        this.userStatus = response;
        this.loyaltyColor = this.userStatus == "REGULAR"?"#F0F8FF":this.userStatus == "BRONZE"?"#CD7F32":this.userStatus == "SILVER"?"#C0C0C0":"#FFD700";
      },
      error: (err) => {
        console.log("Greska");
      }
    }
  );
  }

  logOut(){
    this.authService.logOut();
    this.router.navigateByUrl("/");
  }

  goToAllFlightsPage(){
    this.router.navigateByUrl("flights");
  }


  goToSetPricesPage(){
    this.router.navigateByUrl("set-prices-page");
  }


  goToPurchasedTicketsPage(){
    this.router.navigateByUrl("purchased-tickets-page");
  }

  goToReportPage(){
    this.router.navigateByUrl("report-page");
  }

}
