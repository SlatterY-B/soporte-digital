import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignmentTableComponent } from './assignment-table.component';

describe('AssignmentTableComponent', () => {
  let component: AssignmentTableComponent;
  let fixture: ComponentFixture<AssignmentTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AssignmentTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AssignmentTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
