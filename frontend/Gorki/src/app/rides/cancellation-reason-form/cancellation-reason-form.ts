import { Component, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-cancellation-reason-form',
  imports: [FormsModule],
  templateUrl: './cancellation-reason-form.html',
  styleUrl: './cancellation-reason-form.css',
})
export class CancellationReasonForm {
  reason: string = '';

  @Output() submit = new EventEmitter<string>();
  @Output() close = new EventEmitter<void>();

  onSubmit() {
    this.submit.emit(this.reason);
    this.close.emit();
  }
}
