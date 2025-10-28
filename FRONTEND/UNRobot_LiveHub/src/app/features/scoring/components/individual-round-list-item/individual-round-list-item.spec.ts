import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IndividualRoundListItem } from './individual-round-list-item';

describe('IndividualRoundListItem', () => {
  let component: IndividualRoundListItem;
  let fixture: ComponentFixture<IndividualRoundListItem>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IndividualRoundListItem]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IndividualRoundListItem);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
