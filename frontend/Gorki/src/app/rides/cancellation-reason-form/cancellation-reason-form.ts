import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-cancellation-reason-form',
  imports: [],
  templateUrl: './cancellation-reason-form.html',
  styleUrl: './cancellation-reason-form.css',
})
export class CancellationReasonForm {
  @Output() close = new EventEmitter<void>();

  onSubmit() {
    this.close.emit();
  }
}
