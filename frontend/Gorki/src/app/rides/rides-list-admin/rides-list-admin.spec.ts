import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RidesListAdmin } from './rides-list-admin';

describe('RidesListAdmin', () => {
  let component: RidesListAdmin;
  let fixture: ComponentFixture<RidesListAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RidesListAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RidesListAdmin);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
