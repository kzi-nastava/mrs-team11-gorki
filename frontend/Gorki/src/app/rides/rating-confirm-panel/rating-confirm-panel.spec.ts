import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { vi } from 'vitest';

import { RatingConfirmPanel } from './rating-confirm-panel';

describe('RatingConfirmPanel', () => {
  let component: RatingConfirmPanel;
  let fixture: ComponentFixture<RatingConfirmPanel>;
  let dialogRefMock: any;

  beforeEach(async () => {
    dialogRefMock = {
      close: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [RatingConfirmPanel],
      providers: [
        {
          provide: MatDialogRef,
          useValue: dialogRefMock,
        },
        {
          provide: MAT_DIALOG_DATA,
          useValue: {
            title: 'Ride finished',
            message: 'Your ride was completed successfully.',
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RatingConfirmPanel);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('later should close dialog with false', () => {
    component.later();

    expect(dialogRefMock.close).toHaveBeenCalledWith(false);
  });

  it('rate should close dialog with true', () => {
    component.rate();

    expect(dialogRefMock.close).toHaveBeenCalledWith(true);
  });
});