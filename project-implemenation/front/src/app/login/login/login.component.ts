import {Component, Inject, OnInit} from '@angular/core';
import { FormControl, FormGroup, Validators, FormBuilder } from '@angular/forms';
import {Router} from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LoginCredentials } from 'src/app/model/loginCredentials';
import { AuthService } from 'src/app/services/auth/auth.service';
import { UserService } from 'src/app/services/user/user.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginDataForm: FormGroup; 
  public loading = false;

  constructor(private userService: UserService, private toastr:ToastrService, private router:Router, private authService:AuthService) {}

  ngOnInit(): void {
    this.loginDataForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required]),
    })
  }

  onLogin(){
    this.loading = true;

    let loginCredentials: LoginCredentials = {
      email: this.loginDataForm.value['email'],
      password: this.loginDataForm.value['password']
    }

    this.userService.login(loginCredentials).subscribe({
      next:(res)=>{
        this.loading = false;
        this.toastr.success("Successful login");
        this.authService.setCurrentUser(res);
        if(this.authService.getCurrentUser().userType == "ADMIN")
          this.router.navigateByUrl("set-prices-page");
        else
          this.router.navigateByUrl("flights");
      },
      error:(err)=>{
        console.log(err)
        this.loading = false;
        if(err.status == 403)
          this.toastr.error("User is blocked");
        else
          this.toastr.error("Bad credentials");
      }
    });
  }
}
