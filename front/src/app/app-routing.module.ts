import {RouterModule, Routes} from '@angular/router';
import { LoginComponent } from './login/login.component';
import { LayoutComponent } from './layout/layout.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ProfileComponent } from './profile/profile.component';
import {Component, NgModule} from "@angular/core";
import { AdministrationComponent } from './administration/administration.component';
import { authGuard } from './login/authentification/auth.guard';
import { TypeObjectComponent } from './type-object/type-object.component';
import { ObjectComponent } from './type-object/object/object.component';
import { DetailObjectComponent } from './type-object/object/detail-object/detail-object.component';

const routes: Routes = [
  {
    path: '', redirectTo: 'login', pathMatch: 'full'
  },

  {
    path: 'login',
    component: LoginComponent
  },


  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children:[
      {
        path: 'dashboard',
        component: DashboardComponent
      },

      {
        path: 'profile',
        component: ProfileComponent

      },

       {
        path: 'typeObject',
        component: TypeObjectComponent
      
       },

        { path: 'typeObject/object',
            component: ObjectComponent,
            pathMatch: 'full'

        },
        {
          path:'typeObject/object/detail',
          component: DetailObjectComponent,
          pathMatch: 'full'
        },

       {
        path: 'administration',
        component: AdministrationComponent,
       }

    ]
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    paramsInheritanceStrategy: 'always'
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
