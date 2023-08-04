import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  

  constructor() { }

  getCurrentUser() : any{
    const currentUser = JSON.parse(sessionStorage.getItem("user") as string);
    return currentUser ? currentUser : null;
  }

  setCurrentUser(user:any){
    sessionStorage.setItem("user", JSON.stringify(user));
  }

  logOut() {
    sessionStorage.removeItem("user");
  }
}
