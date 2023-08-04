import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FlightDTO } from 'src/app/model/flightDTO';
import { Location } from '@angular/common';
import { TicketService } from 'src/app/services/ticket/ticket.service';
import { TicketData } from 'src/app/model/ticketData';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-flight-suggestion-dialog',
  templateUrl: './flight-suggestion-dialog.component.html',
  styleUrls: ['./flight-suggestion-dialog.component.scss']
})
export class FlightSuggestionDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: FlightDTO, private dialogRef: MatDialogRef<FlightSuggestionDialogComponent>, private location: Location, private ticketService: TicketService, private toastrService: ToastrService) {
    this.dialogRef.disableClose = true;
  }

  suggestedFlightId: number = this.data.id;

  ngOnInit(){
  }

  onCancel(){
    this.location.back();
  }

  onAccept(){
    this.dialogRef.close(this.suggestedFlightId);
  }
}
