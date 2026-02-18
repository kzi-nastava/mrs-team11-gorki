import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BlockUserDTO } from '../model/ui/block-user-dto';

@Component({
  selector: 'app-block-note',
  imports: [FormsModule],
  templateUrl: './block-note.html',
  styleUrl: './block-note.css',
})
export class BlockNote {
  @Input() blocked!: boolean;
  @Input() userId!: number;
  @Output() close = new EventEmitter<void>();
  @Output() block = new EventEmitter<BlockUserDTO>();
  reason = '';

  onCancel() {
    this.close.emit();
  }

  onBlock(){
    const dto:BlockUserDTO = {
      id: this.userId,
      blockReason: this.reason.trim()
    };
    this.block.emit(dto);
  }
}
