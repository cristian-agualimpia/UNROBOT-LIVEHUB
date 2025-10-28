import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamRegistration } from './team-registration';

describe('TeamRegistration', () => {
  let component: TeamRegistration;
  let fixture: ComponentFixture<TeamRegistration>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TeamRegistration]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TeamRegistration);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
