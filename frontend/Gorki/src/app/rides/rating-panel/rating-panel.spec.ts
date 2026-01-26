import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RatingPanel } from './rating-panel';

describe('RatingPanel', () => {
  let component: RatingPanel;
  let fixture: ComponentFixture<RatingPanel>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RatingPanel]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RatingPanel);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
