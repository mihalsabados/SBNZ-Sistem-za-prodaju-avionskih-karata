import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-buy-ticket-page',
  templateUrl: './buy-ticket-page.component.html',
  styleUrls: ['./buy-ticket-page.component.scss']
})
export class BuyTicketPageComponent {
  public buyTicketForm: FormGroup;

  flightId:number;

  constructor(private toastrService:ToastrService, private router:Router, private authService:AuthService, private route: ActivatedRoute){
    this.flightId = (route.snapshot.paramMap.get('id') as string) as unknown as number;
  }


  ngOnInit(): void {
    this.buyTicketForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),
    })
  }

  onSubmit(){
    
  }
}
