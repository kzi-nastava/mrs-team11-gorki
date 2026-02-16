import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RatingConfirmPanel } from './rating-confirm-panel';

describe('RatingConfirmPanel', () => {
  let component: RatingConfirmPanel;
  let fixture: ComponentFixture<RatingConfirmPanel>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RatingConfirmPanel]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RatingConfirmPanel);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
