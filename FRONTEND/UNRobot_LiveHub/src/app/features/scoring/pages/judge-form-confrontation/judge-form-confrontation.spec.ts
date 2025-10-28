import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JudgeFormConfrontation } from './judge-form-confrontation';

describe('JudgeFormConfrontation', () => {
  let component: JudgeFormConfrontation;
  let fixture: ComponentFixture<JudgeFormConfrontation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JudgeFormConfrontation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JudgeFormConfrontation);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
