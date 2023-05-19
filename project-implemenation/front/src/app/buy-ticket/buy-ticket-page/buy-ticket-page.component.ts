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

  pickedPassengerStatus: string = 'Only for me';
  passengerStatuses: string[] = ['Only for me', 'For another passenger'];

  pickedCardType: string = 'Economic';
  cardTypes: string[] = ['Economic', 'Business'];

  ticketToShow: TicketToShowDTO;

  constructor(private toastrService:ToastrService, private router:Router, private authService:AuthService, private route: ActivatedRoute, private location: Location, private ticketService: TicketService, private responseDialog: MatDialog){
    this.flightId = (route.snapshot.paramMap.get('id') as string) as unknown as number;
    this.destination = (route.snapshot.paramMap.get('destination') as string) as unknown as string;
    this.departure = (route.snapshot.paramMap.get('departure') as string) as unknown as Date;
  }


  ngOnInit(): void {
    this.loggedUser = this.authService.getCurrentUser();
    this.loyaltyColor = this.loggedUser.loyaltyStatus == "REGULAR"?"#F0F8FF":this.loggedUser.loyaltyStatus == "BRONZE"?"#CD7F32":this.loggedUser.loyaltyStatus == "SILVER"?"#C0C0C0":"#FFD700";

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

  onSubmit(){
    let ticketData: TicketData = {
      passengerData: this.anotherPassengerForm.value,
      payerEmail: this.loggedUser.email,
      flightId: this.flightId,
      cardType: this.pickedCardType,
    };
    
    this.ticketService.createTicket(ticketData).subscribe({
      next: (res) => {
        console.log(res)
        this.toastrService.success("Ticket successfully reserved");
        this.ticketToShow = res;
        this.openResponseDialog();
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
    dialogRef.afterClosed().subscribe(() => {
      console.log('izasao');
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
