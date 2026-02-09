import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-ordering-confirmation',
  imports: [],
  templateUrl: './ordering-confirmation.html',
  styleUrl: './ordering-confirmation.css',
})
export class OrderingConfirmation {
  @Output() close = new EventEmitter<void>();
  @Output() confirm = new EventEmitter<void>();
  @Input() price!: number;
}
