import { Component, EventEmitter, Output } from '@angular/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-date-filter',
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    FormsModule,
  ],
  templateUrl: './date-filter.html',
  styleUrl: './date-filter.css'
})
export class DateFilter {
  fromDate: Date | null = null;
  toDate: Date | null = null;

  @Output()
  dateChanged = new EventEmitter<{ from: Date | null; to: Date | null }>();

  emitChange() {
    this.dateChanged.emit({
      from: this.fromDate,
      to: this.toDate
    });
  }
}