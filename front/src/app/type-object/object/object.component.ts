import { Component, OnInit, inject } from '@angular/core';
import { Objects } from './Object';
import { ObjectService } from './ObjectService';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../login/authentification/AuthService';
import { MatDialog } from '@angular/material/dialog';
import { NewObjectComponent } from './new-object/new-object.component';
import { TypeObjects } from '../TypeObjects';
@Component({
  selector: 'app-object',
  templateUrl: './object.component.html',
  styleUrl: './object.component.css'
})
export class ObjectComponent implements OnInit {
  newObject: string | undefined;
  idTypeObjet:string = '';
  idObjet: string = '';
  objects : Objects [] = [];
  authservice = inject(AuthService);
  dialog = inject(MatDialog)
  currentTypeObject !: TypeObjects
  constructor(private objectService: ObjectService,
              private route: ActivatedRoute,
              private router:Router){
  }

  ngOnInit(): void {

    this.route.queryParams.subscribe(param => {
      this.idTypeObjet = param['idTypeObjet'];
      console.log("idTypeObjet: ", this.idTypeObjet);
      if (this.idTypeObjet) {
        this.getAllObjects(Number(this.idTypeObjet));
      }
    });
  }
  
  getAllObjects(idType: number): void {
    idType = parseInt(this.idTypeObjet);
    this.objectService.getAllObjects(idType).subscribe(
      (data: Objects[]) => {
        this.objects = data;
      },
      (error) => {
        console.error('Error fetching objets:', error);
      }
    );
  }

  returnTypeObject():void {
    this.router.navigateByUrl('/typeObject');
  }
  
  onOpenDialog(): void{
    this.dialog.open(NewObjectComponent, {
      data: {newObject: this.newObject}
    });

  }

  openDetailObject(idObjet:number):void{
    this.router.navigate(['typeObject/object/detail'], {
      queryParams: {
        idObjet : idObjet,
        idTypeObjet : this.idTypeObjet
      }
    });
    console.log("Received idObjet", idObjet)

  }
}
