import { TicketService } from './../services/ticket/ticket.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth/auth.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.scss']
})
export class AdminPageComponent implements OnInit {
  loggedUser:any;
  businessForm:FormGroup[] = [];
  economicForm:FormGroup[] = [];

  constructor(private router:Router, private authService:AuthService, private ticketService:TicketService, private toaster:ToastrService){
      
  }


  ngOnInit(): void {
    this.loggedUser = this.authService.getCurrentUser();
    this.getPriceTemplate();
  }
  getPriceTemplate() {
    this.ticketService.getPriceTemplate().subscribe({
      next: (res) => {
        console.log(res);
        this.fillForm(res);
        
      },
      error: (err) => {
        this.toaster.warning("Something went wrong, please try again!");
      }
	  });
  }

  fillForm(res: any) {
    for (const priceTemp of res) {
      if(priceTemp.ticketType == "BUSINESS"){
        this.businessForm.push(
          new FormGroup({
            id: new FormControl(priceTemp.id, [Validators.required]),
            ticketType: new FormControl(priceTemp.ticketType, [Validators.required]),
            minDistance: new FormControl(priceTemp.minDistance, [Validators.required]),
            maxDistance: new FormControl(priceTemp.maxDistance, [Validators.required]),
            price: new FormControl(priceTemp.price, [Validators.required]),
          })
        )
      }
      else if(priceTemp.ticketType == "ECONOMIC"){
        this.economicForm.push(
          new FormGroup({
            id: new FormControl(priceTemp.id, [Validators.required]),
            ticketType: new FormControl(priceTemp.ticketType, [Validators.required]),
            minDistance: new FormControl(priceTemp.minDistance, [Validators.required]),
            maxDistance: new FormControl(priceTemp.maxDistance, [Validators.required]),
            price: new FormControl(priceTemp.price, [Validators.required]),
          })
        )
      }
    }
  }

  saveTemplate() {
    if(this.economicForm.filter(form=>form.invalid).length == 0 && this.businessForm.filter(form=>form.invalid).length == 0){
      let data:any[] = this.businessForm.map(form=>form.value);
      data = data.concat(this.economicForm.map(form=>form.value))
      console.log(data);


      this.ticketService.setPriceTemplates(data).subscribe({
        next: (res) => {
          console.log(res);
          this.toaster.success("Successfully saved price templates!");
        },
        error: (err) => {
          this.toaster.warning("Something went wrong, please try again!");
        }
      });


    }
  }


  logOut(){
    this.authService.logOut();
    this.router.navigateByUrl("/");
  }

}
