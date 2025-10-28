import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JudgeFormIndividual } from './judge-form-individual';

describe('JudgeFormIndividual', () => {
  let component: JudgeFormIndividual;
  let fixture: ComponentFixture<JudgeFormIndividual>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JudgeFormIndividual]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JudgeFormIndividual);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
