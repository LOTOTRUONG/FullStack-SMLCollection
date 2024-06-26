import { AfterViewInit, Component, ElementRef, Inject, ViewChild, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { NewObjectDialogData } from './NewObjectDialogData';
import { NotificationDialogComponent } from '../../../service/notification-dialog/notification-dialog.component';
import { Country } from './Country';
import { TypeObjectService } from '../../TypeObjectService';
import { TypeObjects } from '../../TypeObjects';
import { ActivatedRoute } from '@angular/router';
import { ObjectService } from '../ObjectService';

@Component({
  selector: 'app-new-object',
  templateUrl: './new-object.component.html',
  styleUrl: './new-object.component.css'
})
export class NewObjectComponent implements AfterViewInit {
  @ViewChild('saveProfile') btnSave!: ElementRef<HTMLElement>;
  @ViewChild('editProfile') btnEdit!: ElementRef<HTMLElement>;
  @ViewChild('imgObject') imgObject!: ElementRef<HTMLImageElement>;
  @ViewChild('mediaOverlay') overlay!: ElementRef<HTMLElement>;
  @ViewChild('mediaInput') mediaInput!: ElementRef<HTMLInputElement>;
  objectService = inject(ObjectService);
  typeObjectService = inject(TypeObjectService);
  dialog = inject(MatDialog);
  allCountries: Country[] = [];
  allTypes: TypeObjects[] = [];
  idTypeObjet:string = '';
  currentTypeObject: TypeObjects | undefined;

  constructor(
    public dialogRef:MatDialogRef<NewObjectComponent>,
    @Inject(MAT_DIALOG_DATA) 
    public data:NewObjectDialogData,
    private route: ActivatedRoute,
  ) {}

  onNoClick():void {
    this.dialogRef.close();
  }
  

  ngAfterViewInit(): void {
    this.btnSave.nativeElement.style.display='none';
    this.overlay.nativeElement.style.display='none';

    this.btnEdit.nativeElement.addEventListener('click', () => {
      this.btnEdit.nativeElement.style.display='none';
      this.overlay.nativeElement.style.display='block';
      this.overlay.nativeElement.style.animation='fadeIn(300)'
      this.btnSave.nativeElement.style.display='block';
    })

    this.btnSave.nativeElement.addEventListener('click', () => {
      this.btnSave.nativeElement.style.display='none';
      this.overlay.nativeElement.style.display='none';
      this.btnEdit.nativeElement.style.display='block';
    })

    this.mediaInput.nativeElement.addEventListener('change', (event: Event) => {
      const target = event.target as HTMLInputElement;
      if (target.files && target.files[0]) {
        const reader = new FileReader();
        reader.onload = (e) => {
          if (e.target && e.target.result) {
            this.imgObject.nativeElement.src = e.target.result as string;
          }
        };
        reader.readAsDataURL(target.files[0]);
      }
    });

    this.route.queryParams.subscribe(param => {
      this.idTypeObjet = param['idTypeObjet'];
      console.log("idTypeObjet: ", this.idTypeObjet);
      this.getCurrentTypeObject(Number(this.idTypeObjet))

    });

    this.getAllCountries();
    this.getAllTypeObject();
  }

  onCreateNewObject(): void {
  if (!this.data.libelleObjet) {
    this.dialog.open(NotificationDialogComponent,{
      data: {message: 'Please enter the name of the object'}
    });
    return;
  }

  if (!this.currentTypeObject?.nomTypeObjet) {
    return;
  }

  this.objectService.createNewObject(
    this.data.libelleObjet,
    this.data.nomPays,
    this.currentTypeObject?.nomTypeObjet
  ).subscribe(
    (response: any) => {
        console.log(response);
        this.dialogRef.close();
        window.location.reload();
    }
  );
  }
  

  getAllCountries(){
    this.objectService.getAllCountries().subscribe((data:Country[]) => {
      this.allCountries = data;
      console.log(data);
    });
  }

  getAllTypeObject(){
    this.typeObjectService.getAllTypes().subscribe((dataType:TypeObjects[]) => {
      this.allTypes = dataType;
      console.log(dataType);

    })
  }

  getCurrentTypeObject(id: number): void{
    this.typeObjectService.getCurrentType(id).subscribe(
      (data: TypeObjects) => {
        this.currentTypeObject = data;
      },
      (error) => {
        console.error('Error fetching type object:', error);
      }
    );
  }
  

}