import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AuthService } from '../authentification/AuthService';

@Component({
  selector: 'app-pincode-dialog',
  templateUrl: './pincode-dialog.component.html',
  styleUrl: './pincode-dialog.component.css'
})
export class PincodeDialogComponent {
  pinCode!: number;
  login !: string;

  constructor( private router : Router,
    public dialogRef: MatDialogRef<PincodeDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {login: string},
    private authService : AuthService,
  ) { 
    this.login = data.login;
  }

  onOK(event : Event) {
    event.preventDefault();
    this.authService.validateLogin({ login: this.login, codePin: this.pinCode }).subscribe(()=> {
      this.dialogRef.close();
      this.router.navigate(['/dashboard']);
    })
    };
  }

