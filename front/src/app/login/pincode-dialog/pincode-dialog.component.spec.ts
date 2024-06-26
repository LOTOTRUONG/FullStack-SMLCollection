import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PincodeDialogComponent } from './pincode-dialog.component';

describe('PincodeDialogComponent', () => {
  let component: PincodeDialogComponent;
  let fixture: ComponentFixture<PincodeDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PincodeDialogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PincodeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
