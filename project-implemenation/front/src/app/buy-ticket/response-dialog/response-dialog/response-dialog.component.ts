import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { TicketToShowDTO } from 'src/app/model/ticketToShowDTO';

@Component({
  selector: 'app-response-dialog',
  templateUrl: './response-dialog.component.html',
  styleUrls: ['./response-dialog.component.scss']
})
export class ResponseDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: TicketToShowDTO) {}
}
