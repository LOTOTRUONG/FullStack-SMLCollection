import { Component, OnInit, inject } from '@angular/core';
import { DetailObjects } from './DetailObjectData';
import { ActivatedRoute } from '@angular/router';
import { ObjectService } from '../ObjectService';
import { merge, mergeAll } from 'rxjs';

@Component({
  selector: 'app-detail-object',
  templateUrl: './detail-object.component.html',
  styleUrl: './detail-object.component.css'
})
export class DetailObjectComponent implements OnInit {
  idObjet: string = '';
  nameObjet : DetailObjects | null = null ;
  idType:string = '';
  displayedObjectIds: Set<number> = new Set();
  idAttributType: string = ''
  detailObjects : DetailObjects[] = [];
  objectService= inject(ObjectService);
  route = inject(ActivatedRoute)

ngOnInit(): void {
      this.route.queryParams.subscribe(param => {
        this.idObjet = param['idObjet'];
        this.idType = param['idTypeObjet'];
        if(this.idObjet && this.idType){
          this.getDetailObject(Number(this.idType),Number(this.idObjet));
          this.getObjectById(Number(this.idObjet))
        }

      })
}


getDetailObject(idType:number, idObjet:number):void{
    idObjet = parseInt(this.idObjet);
    this.objectService.getAllAttributObject(idType, idObjet).subscribe((data : DetailObjects[]) => {
      this.detailObjects = data;
    })
}

getObjectById(idObjet: number):void{
  this.objectService.getObjectById(idObjet).subscribe((data : any) => {
    this.nameObjet = data;
  })

}

}
