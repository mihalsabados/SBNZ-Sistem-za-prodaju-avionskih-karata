import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TicketToShowDTO } from 'src/app/model/ticketToShowDTO';
import { Location } from '@angular/common';

@Component({
  selector: 'app-response-dialog',
  templateUrl: './response-dialog.component.html',
  styleUrls: ['./response-dialog.component.scss']
})
export class ResponseDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: TicketToShowDTO, private dialogRef: MatDialogRef<ResponseDialogComponent>, private location: Location) {
    this.dialogRef.disableClose = true;
  }

  displayedColumns: ['name', 'percentage'];

  ngOnInit(){
    this.displayedColumns = ['name', 'percentage'];
  }

  goBackToFlights(){
    this.location.back();
  }
}
