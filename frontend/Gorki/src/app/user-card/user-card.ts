import { Component, EventEmitter, Input, Output } from '@angular/core';
import { GetUserDTO } from '../model/ui/get-user-dto';

@Component({
  selector: 'app-user-card',
  imports: [],
  templateUrl: './user-card.html',
  styleUrl: './user-card.css',
})
export class UserCard {
  @Input() user!: GetUserDTO;
  @Output() openBlock = new EventEmitter<number>();

  openBlockNote() {
    this.openBlock.emit(this.user.id);
  }

}
