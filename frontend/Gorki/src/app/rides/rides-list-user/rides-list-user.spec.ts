import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RidesListUser } from './rides-list-user';

describe('RidesListUser', () => {
  let component: RidesListUser;
  let fixture: ComponentFixture<RidesListUser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RidesListUser]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RidesListUser);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
