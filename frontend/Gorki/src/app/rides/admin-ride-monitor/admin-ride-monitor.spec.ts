import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminRideMonitor } from './admin-ride-monitor';

describe('AdminRideMonitor', () => {
  let component: AdminRideMonitor;
  let fixture: ComponentFixture<AdminRideMonitor>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminRideMonitor]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminRideMonitor);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
