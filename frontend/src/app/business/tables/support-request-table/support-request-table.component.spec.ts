import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupportRequestTableComponent } from './support-request-table.component';

describe('SupportRequestTableComponent', () => {
  let component: SupportRequestTableComponent;
  let fixture: ComponentFixture<SupportRequestTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupportRequestTableComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupportRequestTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
