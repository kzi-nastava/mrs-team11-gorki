import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RideListMap } from './ride-list-map';

describe('RideListMap', () => {
  let component: RideListMap;
  let fixture: ComponentFixture<RideListMap>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RideListMap]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RideListMap);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
