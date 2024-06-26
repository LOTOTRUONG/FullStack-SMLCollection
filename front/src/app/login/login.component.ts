import {HttpClient, HttpErrorResponse, HttpHeaderResponse, HttpResponse} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { Router } from '@angular/router';
import {AuthService} from "./authentification/AuthService";
import {User} from "./authentification/auth";
import { MatDialog } from '@angular/material/dialog';
import { NotificationDialogComponent } from '../service/notification-dialog/notification-dialog.component';
import { PincodeDialogComponent } from './pincode-dialog/pincode-dialog.component';
import { ForgetPWDialogComponent } from './forget-pw-dialog/forget-pw-dialog.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{
  isSignDivVisiable: boolean = false;

  registerForm !: FormGroup;
  loginForm !: FormGroup;
  constructor(private authService:AuthService, private fb:FormBuilder, private  router:Router, private http:HttpClient, private dialog: MatDialog) {
  }
  ngOnInit() {
    this.registerForm = this.fb.group({
      email: ['', Validators.required],
      login: ['', Validators.required],
      password: ['', Validators.required]
    });
    this.loginForm = this.fb.group({
      login: ['', Validators.required],
      password: ['', Validators.required]
    });
  }
  onRegister() {
    if (this.registerForm.valid) {
      const postData = { ...this.registerForm.value };
      this.authService.register(postData as User)
        .subscribe((response: any) => { 
          console.log(response);
          this.dialog.open(NotificationDialogComponent, {
            data: { message: 'Register Success' }
          });
          this.registerForm.reset();
          
      }, error => {
        console.error(error);
        if (error.status === 400 && error.error === 'Login already taken') {
          this.dialog.open(NotificationDialogComponent, {
            data: { message: 'Login already taken' }
          });
        } else if (error.status === 400 && error.error === 'Invalid password') {
          this.dialog.open(NotificationDialogComponent, {
            data: { message: 'Invalid password' }
          });
        } else if (error.status === 400 && error.error === 'Invalid email') {
          this.dialog.open(NotificationDialogComponent, {
            data: { message: 'Invalid email' }
          });
        } else {
          console.log(error);
          alert("Something was wrong");
        }
      });
    }
  }

  onLogin() {
    if (this.loginForm.valid) {
      const loginData = {
        login: this.loginForm.value.login, // Assuming 'username' is used for login
        password: this.loginForm.value.password
      };
      this.authService.login(loginData)
        .subscribe((response: any) => { 
          console.log(response);
          this.openPincodeDialog(loginData.login);
      }, error => {
        console.error(error);
        if (error.status === 403 && error.error === 'Your account has been deactivated') {
          this.dialog.open(NotificationDialogComponent, {
            data: { message: 'Your account has been deactivated' }
          });
        } else if (error.status === 404) {
          this.dialog.open(NotificationDialogComponent, {
            data: { message: 'User not found' }
          });
        } else if (error.status === 403 && error.error === 'Invalid password') {
          this.dialog.open(NotificationDialogComponent, {
            data: { message: 'Invalid password' }
          });
        } else if (error.status === 400 && error.error === 'Please enter login and password') {
          this.dialog.open(NotificationDialogComponent, {
            data: { message: 'Please enter login and password' }
          });
        } else {
          console.log(error);
          this.dialog.open(NotificationDialogComponent, {
            data: { message: 'Something was wrong' }
          });
                
        }
      });
    }
  }

  openPincodeDialog(login: string) {
    this.dialog.open(PincodeDialogComponent, {
      data: { login: login }
    });
  }
  openForgetPWdialog(){
    this.dialog.open(ForgetPWDialogComponent)
  }



  get username(){
    return this.registerForm.controls['username'];
  }

  get email(){
    return this.registerForm.controls['email'];
  }

  get password(){
   return this.registerForm.controls['password'];
  }

}

