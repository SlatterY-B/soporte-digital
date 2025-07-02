import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignAgentPanelComponent } from './assign-agent-panel.component';

describe('AssignAgentPanelComponent', () => {
  let component: AssignAgentPanelComponent;
  let fixture: ComponentFixture<AssignAgentPanelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AssignAgentPanelComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AssignAgentPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
