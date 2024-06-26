import { Component, inject } from '@angular/core';
import { TypeObjectService } from '../type-object/TypeObjectService';
import { TypeObjects } from '../type-object/TypeObjects';
import { AuthService } from '../login/authentification/AuthService';
import { Objects } from '../type-object/object/Object';
import { ObjectService } from '../type-object/object/ObjectService';
import { window } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent{
  loggedUser: any;
  typeObjects:TypeObjects[] = [];
  selectedTypeObject: TypeObjects | null = null;
  objects : Objects[] = [];
  token = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJqd3QtdG9rZW4iLCJzdWIiOiJhZG1pbiIsImdyb3VwcyI6WyJzdXBlcmFkbWluIl0sImlhdCI6MTcxMjA0OTk2MiwiZXhwIjozMzI0ODA0OTk2MiwianRpIjoiZjVlYWNmYzUtMmRkZC00OGFlLWEwOWYtNjM3ZjliOTQ4NGVhIn0.KKD6sWVA-e7t39bQYbNFKPP8QnCCGZ8UPwkFjkjO-IbHor7oTHSBKEAoj31OFnS27VagR_-dHSgCegThsJgsTMmylGW7dbzHsH4r7v47UdVwDGXk2khLrKfgAyO0wxB5SB2pkzo-_bXvHqcmzWxY08NVRghGxayDxcH3LpBzAszMylRssb7YoWThdgYdDlNY2voGFBfycA_wQDXmHIfXgUvimn2-zL5A5m5vPwyi2hiEgC-pc3Iz72u6A6J586XknD5w1A_KMtxirhrZK9UZ2wukw7kUuXEdf0K2kfJtPTC6hAPX_8cgkHG2Ovff89rSoZhhUsnyZbw1u0Buks2Bfg';
  typeObjectService = inject(TypeObjectService);
  objectService  = inject(ObjectService);
  authservice = inject(AuthService);
  constructor() {
   this.authservice.getCurrentAuthUser().subscribe((response) => {
     console.log(response); 
     this.loggedUser = response;
   })
 }

 ngOnInit(): void {
  this.getAllTypeObjets();
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
  
  selectTypeObject(typeObject : TypeObjects):void{
    this.selectedTypeObject = typeObject;
     if (this.selectedTypeObject !== null) {
      this.objectService.getAllObjects(this.selectedTypeObject.idTypeObjet)
        .subscribe(
          (data: Objects[]) => {
            this.objects = data;
          },
          (error) => {
            console.error('Error fetching objects:', error);
            this.objects = []
          }
        );
    }
  }

}
