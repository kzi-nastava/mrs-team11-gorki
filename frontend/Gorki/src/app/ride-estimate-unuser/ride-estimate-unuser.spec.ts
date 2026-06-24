import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RideEstimateCardComponent } from './ride-estimate-unuser';

describe('RideEstimateCardComponent', () => {
  let component: RideEstimateCardComponent;
  let fixture: ComponentFixture<RideEstimateCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RideEstimateCardComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(RideEstimateCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});