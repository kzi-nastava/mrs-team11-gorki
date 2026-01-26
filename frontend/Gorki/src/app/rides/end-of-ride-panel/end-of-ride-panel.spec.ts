import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EndOfRidePanel } from './end-of-ride-panel';

describe('EndOfRidePanel', () => {
  let component: EndOfRidePanel;
  let fixture: ComponentFixture<EndOfRidePanel>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EndOfRidePanel]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EndOfRidePanel);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
