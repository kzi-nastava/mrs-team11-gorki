import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderingConfirmation } from './ordering-confirmation';

describe('OrderingConfirmation', () => {
  let component: OrderingConfirmation;
  let fixture: ComponentFixture<OrderingConfirmation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrderingConfirmation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrderingConfirmation);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
