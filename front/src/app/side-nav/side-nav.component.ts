import { Component, PLATFORM_ID, Inject, OnInit, inject } from '@angular/core';
import { Router} from '@angular/router';
import { isPlatformBrowser } from '@angular/common';
import { AuthService } from '../login/authentification/AuthService';


@Component({
  selector: 'app-side-nav',
  templateUrl: './side-nav.component.html',
  styleUrl: './side-nav.component.css'
})
export class SideNavComponent implements OnInit {
  loggedUser: any;
  authservice = inject(AuthService);
  router = inject(Router);


  constructor() {
    this.authservice.getCurrentAuthUser().subscribe((response) => {
      console.log(response); 
      this.loggedUser = response;
    })
  }

  onLogoff() {
    this.authservice.logout;
  }

  ngOnInit(): void {
  }

}
