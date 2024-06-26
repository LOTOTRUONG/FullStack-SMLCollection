import { Component, OnInit, inject } from '@angular/core';
import { AdminService } from './AdminService';
import { Users } from './Users';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../service/confirm-dialog/confirm-dialog.component';
import { AuthService } from '../login/authentification/AuthService';


@Component({
  selector: 'app-administration',
  templateUrl: './administration.component.html',
  styleUrl: './administration.component.css'
})
export class AdministrationComponent implements OnInit {

  users : Users[] = [];
  selectedUser: Users | null = null;
  token = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJqd3QtdG9rZW4iLCJzdWIiOiJhZG1pbiIsImdyb3VwcyI6WyJzdXBlcmFkbWluIl0sImlhdCI6MTcxMjA0OTk2MiwiZXhwIjozMzI0ODA0OTk2MiwianRpIjoiZjVlYWNmYzUtMmRkZC00OGFlLWEwOWYtNjM3ZjliOTQ4NGVhIn0.KKD6sWVA-e7t39bQYbNFKPP8QnCCGZ8UPwkFjkjO-IbHor7oTHSBKEAoj31OFnS27VagR_-dHSgCegThsJgsTMmylGW7dbzHsH4r7v47UdVwDGXk2khLrKfgAyO0wxB5SB2pkzo-_bXvHqcmzWxY08NVRghGxayDxcH3LpBzAszMylRssb7YoWThdgYdDlNY2voGFBfycA_wQDXmHIfXgUvimn2-zL5A5m5vPwyi2hiEgC-pc3Iz72u6A6J586XknD5w1A_KMtxirhrZK9UZ2wukw7kUuXEdf0K2kfJtPTC6hAPX_8cgkHG2Ovff89rSoZhhUsnyZbw1u0Buks2Bfg';
  today = new Date();  
  loggedUser:any;
  adminService = inject(AdminService);
  dialog = inject(MatDialog);
  authservice = inject(AuthService);

  constructor() {
    this.authservice.getCurrentAuthUser().subscribe((response) => {
      console.log(response); 
      this.loggedUser = response;
    })
  }

  ngOnInit(): void {
      this.getUsers();
  }

  getUsers():void{
    this.adminService.getAllUsers(this.token).subscribe(users => this.users = users)
  }

  selectUser(user:Users):void{
    this.selectedUser = user;
  }
  deactivateUsers(): void {
    if (this.selectedUser) { // Add null check
      const dialogRef = this.dialog.open(ConfirmDialogComponent, {
        width: '300px',
        data: { message: 'Are you sure you want to deactivate this user?' }
      });
  
      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          if (this.selectedUser) { // Add null check
            this.adminService.deactivateUser(this.selectedUser.login, this.token).subscribe(
              response => {
                console.log(response);
                console.log('User deactivated successfully');
                this.getUsers(); // Reset page
                this.selectedUser = null; // Reset selectedUser after deactivation
              }
            );
          }
        } else {
          console.error('User cancellation');
        }
      });
    } else {
      console.error('No user selected to deactivate');
    }
  }
  
reactivateUsers():void{
  if (this.selectedUser) {
   const dialogRef = this.dialog.open(ConfirmDialogComponent,{
     width: '300px',
     data: {message: 'Are you sure you want to reactivate this user?'}
   });

   dialogRef.afterClosed().subscribe(result => {
     if(result) {
      if (this.selectedUser) { // Add null check
        this.adminService.reactivateUser(this.selectedUser.login, this.token).subscribe(
          response => {
            console.log(response);
            console.log('User reactivated successfully');
            this.getUsers(); // Reset page
            this.selectedUser = null; // Reset selectedUser after deactivation
          }
        );
      }
     }else {
       console.error('User cancellation');
     }
   });
 }
   else {
     console.error('No user selected to reactivate');
   }
}

updateUsers():void{
  if (this.selectedUser) {
   const dialogRef = this.dialog.open(ConfirmDialogComponent,{
     width: '300px',
     data: {message: 'Are you sure you want to reactivate this user?'}
   });

   dialogRef.afterClosed().subscribe(result => {
     if(result) {
      if (this.selectedUser) { // Add null check
        this.adminService.updateUser(this.selectedUser.login, this.token).subscribe(
          response => {
            console.log(response);
            console.log('User reactivated successfully');
            this.getUsers(); // Reset page
            this.selectedUser = null; // Reset selectedUser after deactivation
          }
        );
      }
     }else {
       console.error('User cancellation');
     }
   });
 }
   else {
     console.error('No user selected to reactivate');
   }
}


}
