import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScoringHub } from './scoring-hub';

describe('ScoringHub', () => {
  let component: ScoringHub;
  let fixture: ComponentFixture<ScoringHub>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ScoringHub]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ScoringHub);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
