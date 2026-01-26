import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavouriteRouteCard } from './favourite-route-card';

describe('FavouriteRouteCard', () => {
  let component: FavouriteRouteCard;
  let fixture: ComponentFixture<FavouriteRouteCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FavouriteRouteCard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FavouriteRouteCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
