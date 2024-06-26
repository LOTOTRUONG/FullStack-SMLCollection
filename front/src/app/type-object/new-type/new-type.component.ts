import { AfterViewInit, Component, ElementRef, Inject, ViewChild, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { NewTypeDialogData } from './NewTypeDialogData';
import { NewTypeService } from './NewType.service';
import { NotificationDialogComponent } from '../../service/notification-dialog/notification-dialog.component';

@Component({
  selector: 'app-new-type',
  templateUrl: './new-type.component.html',
  styleUrl: './new-type.component.css'
})
export class NewTypeComponent implements AfterViewInit {
  @ViewChild('saveProfile') btnSave!: ElementRef<HTMLElement>;
  @ViewChild('editProfile') btnEdit!: ElementRef<HTMLElement>;
  @ViewChild('imgObject') imgObject!: ElementRef<HTMLImageElement>;
  @ViewChild('mediaOverlay') overlay!: ElementRef<HTMLElement>;
  @ViewChild('mediaInput') mediaInput!: ElementRef<HTMLInputElement>;
  newTypeService = inject(NewTypeService);
  dialog = inject(MatDialog);

  constructor(
    public dialogRef:MatDialogRef<NewTypeComponent>,
    @Inject(MAT_DIALOG_DATA) 
    public data:NewTypeDialogData,
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
  }

  onCreateNewType(): void {
    this.newTypeService.createTypeObject(this.data.nameType).subscribe(
      (response: any) => {
          console.log(response);
          this.dialogRef.close();
          window.location.reload();
      },
      (error) => {
        if (error.status === 404 && error.error === 'Please enter the name of type object') {
          this.dialog.open(NotificationDialogComponent,{
            data: {message: 'Please enter the name of type object'}
          })
        } else if(error.status === 409 && error.error === 'This name is duplicated') {
          this.dialog.open(NotificationDialogComponent,{
            data: {message: 'This name is duplicated'}
          })
        }
      }
    );
  }
  
  
}
