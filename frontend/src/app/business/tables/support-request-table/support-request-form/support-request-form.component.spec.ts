import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupportRequestFormComponent } from './support-request-form.component';

describe('SupportRequestFormComponent', () => {
  let component: SupportRequestFormComponent;
  let fixture: ComponentFixture<SupportRequestFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupportRequestFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupportRequestFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
