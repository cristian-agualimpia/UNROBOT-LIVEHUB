import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoundList } from './round-list';

describe('RoundList', () => {
  let component: RoundList;
  let fixture: ComponentFixture<RoundList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoundList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoundList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
