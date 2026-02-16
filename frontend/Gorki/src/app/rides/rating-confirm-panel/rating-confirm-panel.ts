import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

export type RateConfirmData = {
  title?: string;
  message?: string;
};
@Component({
  selector: 'app-rating-confirm-panel',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatButtonModule, MatIconModule],
  templateUrl: './rating-confirm-panel.html',
  styleUrls: ['./rating-confirm-panel.css'],
})
export class RatingConfirmPanel {
  constructor(
    private ref: MatDialogRef<RatingConfirmPanel>,
    @Inject(MAT_DIALOG_DATA) public data: RateConfirmData
  ) {}

  later() { this.ref.close(false); }
  rate() { this.ref.close(true); }
}
