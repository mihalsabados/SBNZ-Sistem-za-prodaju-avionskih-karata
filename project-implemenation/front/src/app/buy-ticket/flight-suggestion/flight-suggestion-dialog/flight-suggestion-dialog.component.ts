import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FlightDTO } from 'src/app/model/flightDTO';
import { Location } from '@angular/common';

@Component({
  selector: 'app-flight-suggestion-dialog',
  templateUrl: './flight-suggestion-dialog.component.html',
  styleUrls: ['./flight-suggestion-dialog.component.scss']
})
export class FlightSuggestionDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: FlightDTO[], private dialogRef: MatDialogRef<FlightSuggestionDialogComponent>, private location: Location) {
    this.dialogRef.disableClose = true;
  }

  displayedColumns = ['destination', 'distance', 'price', 'departure', 'soldTickets', 'numberOfSeats', 'popular'];

  ngOnInit(){
    this.displayedColumns = ['destination', 'distance', 'price', 'departure', 'soldTickets', 'numberOfSeats', 'popular'];
  }
}
