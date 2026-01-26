import { Component } from '@angular/core';

@Component({
  selector: 'app-ordering-confirmation',
  imports: [],
  templateUrl: './ordering-confirmation.html',
  styleUrl: './ordering-confirmation.css',
})
export class OrderingConfirmation {
  isModalOpen: boolean = false;

  closeModal() {
    this.isModalOpen = false;
  }

  confirmOrder() {
    window.location.reload();
  }
}
