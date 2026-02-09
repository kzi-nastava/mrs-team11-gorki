import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CancellationReasonForm } from './cancellation-reason-form';

describe('CancellationReasonForm', () => {
  let component: CancellationReasonForm;
  let fixture: ComponentFixture<CancellationReasonForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CancellationReasonForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CancellationReasonForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
