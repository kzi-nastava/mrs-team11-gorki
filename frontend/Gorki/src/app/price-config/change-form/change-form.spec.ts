import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangeForm } from './change-form';

describe('ChangeForm', () => {
  let component: ChangeForm;
  let fixture: ComponentFixture<ChangeForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChangeForm]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChangeForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
