import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TypeObjectComponent } from './type-object.component';

describe('TypeObjectComponent', () => {
  let component: TypeObjectComponent;
  let fixture: ComponentFixture<TypeObjectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeObjectComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TypeObjectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
