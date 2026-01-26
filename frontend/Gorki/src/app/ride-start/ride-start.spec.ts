import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RideStart } from './ride-start';

describe('RideStart', () => {
  let component: RideStart;
  let fixture: ComponentFixture<RideStart>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RideStart]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideStart);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
