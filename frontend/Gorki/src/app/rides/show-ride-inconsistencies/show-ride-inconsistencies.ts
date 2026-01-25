import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-show-ride-inconsistencies',
  standalone: true,
  imports: [],
  templateUrl: './show-ride-inconsistencies.html',
  styleUrl: './show-ride-inconsistencies.css',
})
export class ShowRideInconsistencies {
  @Input() passenger: any;
  @Output() close = new EventEmitter<void>();

  onCancel() {
    this.close.emit();
  }
}
