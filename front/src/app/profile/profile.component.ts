import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../login/authentification/AuthService';
import { ProfileService } from './ProfileService';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../service/confirm-dialog/confirm-dialog.component';


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
  loggedUser: any;
  profileForm: FormGroup;
  fb = inject(FormBuilder);
  authservice = inject(AuthService);
  profileService = inject(ProfileService);
  dialog = inject(MatDialog)

  constructor() {
    this.authservice.getCurrentAuthUser().subscribe((response) => {
      console.log(response); 
      this.loggedUser = response;
    })

    this.profileForm = this.fb.group({
      username: [{ value: this.loggedUser?.login, disabled: true }],
      email: [this.loggedUser?.email, [Validators.required, Validators.email]],
      newPassword: [''],
      repeatPassword: ['']
    });
  }

  submitForm(): void {
    
        const dialogRef = this.dialog.open(ConfirmDialogComponent, {
          width: '300px',
          data: {message: 'Are you sure you want to update these informations'}
        });
        dialogRef.afterClosed().subscribe(result => {
          if (result) {
            // Update email only if it's changed
            if (this.loggedUser.email !== this.profileForm.value.email) {
              this.profileService.requestEmail(this.loggedUser.login, this.profileForm.value.email).subscribe(response => {
                console.log(response);
                console.log('Please check your email: ', this.profileForm.value.email);
              });
            }
    
            // Update password only if new password is provided
            const newPassword = this.profileForm.value.newPassword.trim();
            if (newPassword !== '') {
              if (newPassword !== this.profileForm.value.repeatPassword) {
                console.log('Your password does not match');
              } else if (newPassword.length < 8) {
                console.log('Password must be at least 8 characters long');
              } else {
                this.profileService.requestPassword(this.loggedUser.login, newPassword).subscribe(response => {
                  console.log(response);
                  console.log('Password updated successfully!');
                });
              }
            }
          }
        });
      }
    }