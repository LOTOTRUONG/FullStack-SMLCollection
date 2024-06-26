import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ForgetPWDialogComponent } from './forget-pw-dialog.component';

describe('ForgetPWDialogComponent', () => {
  let component: ForgetPWDialogComponent;
  let fixture: ComponentFixture<ForgetPWDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ForgetPWDialogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ForgetPWDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
