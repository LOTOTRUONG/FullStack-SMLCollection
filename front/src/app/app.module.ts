import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatButtonModule} from '@angular/material/button';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatSelectModule} from '@angular/material/select';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatDialogModule} from '@angular/material/dialog';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HeaderComponent } from './header/header.component';
import { LayoutComponent } from './layout/layout.component';
import { ProfileComponent } from './profile/profile.component';
import { SideNavComponent } from './side-nav/side-nav.component';
import { RouterModule } from '@angular/router';
import {AppRoutingModule} from "./app-routing.module"; // Import RouterModule for routing
import { AdministrationComponent } from './administration/administration.component';
import { PincodeDialogComponent } from './login/pincode-dialog/pincode-dialog.component';
import { JwtHelperService, JwtModule } from '@auth0/angular-jwt';
import { NewTypeComponent } from './type-object/new-type/new-type.component';
import { TypeObjectComponent } from './type-object/type-object.component';
import { ForgetPWDialogComponent } from './login/forget-pw-dialog/forget-pw-dialog.component';
import { ObjectComponent } from './type-object/object/object.component';
import { NewObjectComponent } from './type-object/object/new-object/new-object.component';
import { DetailObjectComponent } from './type-object/object/detail-object/detail-object.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    HeaderComponent,
    LayoutComponent,
    ProfileComponent,
    SideNavComponent,
    TypeObjectComponent,
    NewTypeComponent,
    AdministrationComponent,
    PincodeDialogComponent,
    ForgetPWDialogComponent,
    ObjectComponent,
    NewObjectComponent,
    DetailObjectComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    RouterModule,
    FormsModule,
    AppRoutingModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatDialogModule,
    MatCheckboxModule,
    ReactiveFormsModule,
    MatSelectModule,
    JwtModule.forRoot({ 
      config: {
        tokenGetter: () => {
          return localStorage.getItem('token');
        },
        allowedDomains: ['http://localhost:4200/'], 
        disallowedRoutes: [''], 
      },
    }),
  ],
  providers: [
    JwtHelperService, 
  ],
 
  bootstrap: [AppComponent]
})
export class AppModule { }
