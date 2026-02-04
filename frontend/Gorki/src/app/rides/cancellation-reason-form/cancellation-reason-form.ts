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
    const r = this.reason.trim();
    if (!r) return;
    this.submit.emit(this.reason);
    //this.close.emit();
  }

  onClose() {
    this.close.emit();
  }

}
