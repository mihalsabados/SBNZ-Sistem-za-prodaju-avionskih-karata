import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login/login.component';
import { BuyTicketPageComponent } from './buy-ticket/buy-ticket-page/buy-ticket-page.component';
import { FlightsPageComponent } from './buy-ticket/flights-page/flights-page.component';

const routes: Routes = [
  {
    path:"",
    component: LoginComponent
  },
  {
    path:"flights",
    component: FlightsPageComponent
  },
   {
    path:"buy-ticket",
    component: BuyTicketPageComponent
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
