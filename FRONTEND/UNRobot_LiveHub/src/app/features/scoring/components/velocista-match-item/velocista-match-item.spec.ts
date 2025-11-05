import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VelocistaMatchItem } from './velocista-match-item';

describe('VelocistaMatchItem', () => {
  let component: VelocistaMatchItem;
  let fixture: ComponentFixture<VelocistaMatchItem>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VelocistaMatchItem]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VelocistaMatchItem);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
