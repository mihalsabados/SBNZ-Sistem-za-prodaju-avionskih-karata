import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/services/auth/auth.service';
import { Location } from '@angular/common';
import { TicketData } from 'src/app/model/ticketData';
import { TicketService } from 'src/app/services/ticket/ticket.service';
import { MatDialog } from '@angular/material/dialog';
import { ResponseDialogComponent } from '../response-dialog/response-dialog/response-dialog.component';
import { TicketToShowDTO } from 'src/app/model/ticketToShowDTO';
import { FlightSuggestionDialogComponent } from '../flight-suggestion/flight-suggestion-dialog/flight-suggestion-dialog.component';
import { FlightDTO } from 'src/app/model/flightDTO';
import { UserService } from 'src/app/services/user/user.service';
import { catchError, throwError } from 'rxjs';

@Component({
  selector: 'app-buy-ticket-page',
  templateUrl: './buy-ticket-page.component.html',
  styleUrls: ['./buy-ticket-page.component.scss']
})
export class BuyTicketPageComponent {
  public anotherPassengerForm: FormGroup;
  loggedUser:any;
  flightId:number;
  destination: string;
  departure: Date;
  loyaltyColor: string;
  userStatus:string;

  pickedPassengerStatus: string = 'Only for me';
  passengerStatuses: string[] = ['Only for me', 'For another passenger'];

  pickedCardType: string = 'Economic';
  cardTypes: string[] = ['Economic', 'Business'];

  ticketToShow: TicketToShowDTO;
  flightSuggestion: FlightDTO;

  loading: boolean = false;

  constructor(private toastrService:ToastrService, private router:Router, private authService:AuthService, private route: ActivatedRoute, private location: Location, private ticketService: TicketService, private responseDialog: MatDialog, private flightSuggestionDialog: MatDialog, private userService:UserService){
    this.flightId = (route.snapshot.paramMap.get('id') as string) as unknown as number;
    this.destination = (route.snapshot.paramMap.get('destination') as string) as unknown as string;
    this.departure = (route.snapshot.paramMap.get('departure') as string) as unknown as Date;
  }


  ngOnInit(): void {
    this.loggedUser = this.authService.getCurrentUser();
    this.getUserStatus();

    this.anotherPassengerForm = new FormGroup({
      emailPassenger: new FormControl('', [Validators.required, Validators.email]),
      firstNamePassenger: new FormControl('', [Validators.required]),
      lastNamePassenger: new FormControl('', [Validators.required]),
      addressPassenger: new FormControl('', [Validators.required]),
      phoneNumberPassenger: new FormControl('', [Validators.required]),
      placePassenger: new FormControl('', [Validators.required]),
    })

    this.passengerIsPayer();
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

  onSubmit(){
    this.loading = true;

    let ticketData: TicketData = {
      passengerData: this.anotherPassengerForm.value,
      payerEmail: this.loggedUser.email,
      flightId: this.flightId,
      cardType: this.pickedCardType,
    };
    
    this.ticketService.createTicket(ticketData).subscribe({
      next: (res) => {
        this.loading = false;
        if (typeof res === 'object' && res !== null && 'discounts' in res) {
          this.toastrService.success("Ticket successfully reserved");
          this.ticketToShow = res;
          console.log(this.ticketToShow)
          this.openResponseDialog();
        }

        else{
          this.flightSuggestion = res;
          console.log(this.flightSuggestion);
          this.openFlightSuggestionDialog();
        }
        
      },
      error: (err) => {
        this.toastrService.warning("Something went wrong, please try again!");
      }
	  });
  }

  openResponseDialog() {
    const dialogRef = this.responseDialog.open(ResponseDialogComponent, {
      data: this.ticketToShow,
    });
  }

  openFlightSuggestionDialog() {
    const dialogRef = this.flightSuggestionDialog.open(FlightSuggestionDialogComponent, {
      data: this.flightSuggestion,
    });
    dialogRef.afterClosed().subscribe(suggestedFlightId => {
      if(suggestedFlightId)
        this.acceptSuggestedFlight(suggestedFlightId);
    });
  }

  acceptSuggestedFlight(suggestedFlightId: number){
    this.loading = true;
    let ticketData: TicketData = {
      passengerData: this.anotherPassengerForm.value,
      payerEmail: this.loggedUser.email,
      flightId: suggestedFlightId,
      cardType: this.pickedCardType,
    };
    
    this.ticketService.acceptSuggestedFlight(ticketData).subscribe({
      next: (res) => {
        this.loading = false;
        this.toastrService.success("Ticket successfully reserved");
        this.ticketToShow = res;
        console.log(this.ticketToShow);
        this.openResponseDialog();
      },
      error: (err) => {
        this.toastrService.warning("Something went wrong, please try again!");
      }
	  });
  }

  goBack(): void {
    this.location.back();
  }

  cancel(){
    this.location.back();
  }

  onClickOnlyForMe(){
    this.anotherPassengerForm.reset();
    this.passengerIsPayer();
  }

  onClickForAnotherPassenger(){
    this.anotherPassengerForm.reset();
  }

  passengerIsPayer(){
    this.anotherPassengerForm.patchValue({
      emailPassenger: this.loggedUser.email,
      firstNamePassenger: this.loggedUser.firstName,
      lastNamePassenger: this.loggedUser.lastName,
      addressPassenger: this.loggedUser.address,
      phoneNumberPassenger: this.loggedUser.phoneNumber,
      placePassenger: this.loggedUser.place
    });
  }
}
