import { Component, PLATFORM_ID, Inject, inject, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { TypeObjects } from './TypeObjects';
import { TypeObjectService } from './TypeObjectService';
import { AuthService } from '../login/authentification/AuthService';
import { NewTypeComponent } from './new-type/new-type.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-type-object',
  templateUrl: './type-object.component.html',
  styleUrls: ['./type-object.component.css']
})
export class TypeObjectComponent implements OnInit{
  newType: string | undefined;
  loggedUser: any;
  typeObjects:TypeObjects[] = [];
  token = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJqd3QtdG9rZW4iLCJzdWIiOiJhZG1pbiIsImdyb3VwcyI6WyJzdXBlcmFkbWluIl0sImlhdCI6MTcxMjA0OTk2MiwiZXhwIjozMzI0ODA0OTk2MiwianRpIjoiZjVlYWNmYzUtMmRkZC00OGFlLWEwOWYtNjM3ZjliOTQ4NGVhIn0.KKD6sWVA-e7t39bQYbNFKPP8QnCCGZ8UPwkFjkjO-IbHor7oTHSBKEAoj31OFnS27VagR_-dHSgCegThsJgsTMmylGW7dbzHsH4r7v47UdVwDGXk2khLrKfgAyO0wxB5SB2pkzo-_bXvHqcmzWxY08NVRghGxayDxcH3LpBzAszMylRssb7YoWThdgYdDlNY2voGFBfycA_wQDXmHIfXgUvimn2-zL5A5m5vPwyi2hiEgC-pc3Iz72u6A6J586XknD5w1A_KMtxirhrZK9UZ2wukw7kUuXEdf0K2kfJtPTC6hAPX_8cgkHG2Ovff89rSoZhhUsnyZbw1u0Buks2Bfg';
  dialog = inject(MatDialog);
  typeObjectService = inject(TypeObjectService);
  authservice = inject(AuthService);
  router = inject(Router);
   constructor() {
    this.authservice.getCurrentAuthUser().subscribe((response) => {
      console.log(response); 
      this.loggedUser = response;
    })
  }
  ngOnInit(): void {
      this.getAllTypeObjets();
  }

  onOpenDialog(): void{
    this.dialog.open(NewTypeComponent, {
      data: {newType: this.newType}
    });

  }

  getAllTypeObjets(): void {
    this.typeObjectService.getAllTypes().subscribe(
      (data: TypeObjects[]) => {
        this.typeObjects = data;
      },
      (error) => {
        console.error('Error fetching type objets:', error);
      }
    );
  }

  openObjects(idTypeObjet: number): void {
  this.router.navigate(['typeObject/object'], {
    queryParams: {
      idTypeObjet: idTypeObjet
    }
  });
  console.log("Received idTypeObject", idTypeObjet)
  }
}
