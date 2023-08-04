import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { BuyTicketPageComponent } from './buy-ticket/buy-ticket-page/buy-ticket-page.component';
import { LoginComponent } from './login/login/login.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { HttpClientModule } from '@angular/common/http';
import { ToastrModule } from 'ngx-toastr';
import { FlightsPageComponent } from './buy-ticket/flights-page/flights-page.component';
import {MatTableModule} from '@angular/material/table';
import {MatRadioModule} from '@angular/material/radio';
import { FormsModule } from '@angular/forms';
import { ResponseDialogComponent } from './buy-ticket/response-dialog/response-dialog/response-dialog.component';
import {MatDialogModule} from "@angular/material/dialog";
import { FlightSuggestionDialogComponent } from './buy-ticket/flight-suggestion/flight-suggestion-dialog/flight-suggestion-dialog.component'
import {MatGridListModule} from '@angular/material/grid-list';
import { AdminPageComponent } from './admin-page/admin-page.component';
import { NavbarComponent } from './navbar/navbar.component';
import {MatToolbarModule} from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { PurchasedTicketsPageComponent } from './buy-ticket/purchased-tickets-page/purchased-tickets-page.component';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatSelectModule} from '@angular/material/select';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import { AdminReportPageComponent } from './admin-report-page/admin-report-page.component';
import { ChartModule } from 'primeng/chart';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';


@NgModule({
  declarations: [
    AppComponent,
    BuyTicketPageComponent,
    LoginComponent,
    FlightsPageComponent,
    ResponseDialogComponent,
    FlightSuggestionDialogComponent,
    AdminPageComponent,
    NavbarComponent,
    PurchasedTicketsPageComponent,
    AdminReportPageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatCardModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatProgressBarModule,
    HttpClientModule,
    MatTableModule,
    MatRadioModule,
    FormsModule,
    MatDialogModule,
    MatGridListModule,
    MatToolbarModule,
    MatTooltipModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatAutocompleteModule,
    MatButtonToggleModule,
    MatSelectModule,
    ChartModule,
    MatProgressSpinnerModule,
    ToastrModule.forRoot({
      timeOut: 5000,
      positionClass: 'toast-bottom-center',
      preventDuplicates: true,
    })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
