import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavouriteRoutes } from './favourite-routes';

describe('FavouriteRoutes', () => {
  let component: FavouriteRoutes;
  let fixture: ComponentFixture<FavouriteRoutes>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FavouriteRoutes]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FavouriteRoutes);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
