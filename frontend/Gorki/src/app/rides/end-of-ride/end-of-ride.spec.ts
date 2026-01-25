import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EndOfRide } from './end-of-ride';

describe('EndOfRide', () => {
  let component: EndOfRide;
  let fixture: ComponentFixture<EndOfRide>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EndOfRide]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EndOfRide);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
