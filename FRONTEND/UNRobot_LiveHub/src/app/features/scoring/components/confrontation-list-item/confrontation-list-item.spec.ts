import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfrontationListItem } from './confrontation-list-item';

describe('ConfrontationListItem', () => {
  let component: ConfrontationListItem;
  let fixture: ComponentFixture<ConfrontationListItem>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfrontationListItem]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfrontationListItem);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
