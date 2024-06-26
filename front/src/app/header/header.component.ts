import { Component, PLATFORM_ID, Inject, inject } from '@angular/core';
import { Router} from '@angular/router';
import { isPlatformBrowser } from '@angular/common';
import { AuthService } from '../login/authentification/AuthService';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent  {
  loggedUser: any;
  router = inject(Router);
  authservice = inject(AuthService)

  constructor() {
    this.authservice.getCurrentAuthUser().subscribe((response) => {
      console.log(response); 
      this.loggedUser = response;
    })
  }




  toggleDropdown(event: MouseEvent): void {
    const dropdownElement = (event.currentTarget as HTMLElement).querySelector('.dropdown');
    if (dropdownElement) {
      dropdownElement.classList.toggle('dropdown--active');
    }
  }

  onProfile(){
    this.router.navigateByUrl('/profile');
  }
  onDashboard(){
    this.router.navigateByUrl('/dashboard');
  }
  onAdmin(){
    this.router.navigateByUrl('/administration');
  }
  onLogoff() {
    this.authservice.logout();
  }

}
