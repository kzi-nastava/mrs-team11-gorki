import { ChangeDetectorRef, Component, ElementRef, ViewChild } from '@angular/core';
import { GetUserDTO } from '../model/ui/get-user-dto';
import { BlockUserService } from '../service/block-user-service';
import { UserCard } from "../user-card/user-card";
import { BlockNote } from '../block-note/block-note';
import { BlockUserDTO } from '../model/ui/block-user-dto';

@Component({
  selector: 'app-admin-block-user',
  imports: [UserCard, BlockNote],
  templateUrl: './admin-block-user.html',
  styleUrl: './admin-block-user.css',
})
export class AdminBlockUser {
  @ViewChild('carousel', { static: true })
  carousel!: ElementRef<HTMLDivElement>;
  users!:GetUserDTO[];
  selectedUserId!: number;
  selectedUserBlocked!: boolean;
  isBlockNoteOpen = false;

  scrollLeft() {
    this.carousel.nativeElement.scrollBy({
      left: -300,
      behavior: 'smooth',
    });
  }

  scrollRight() {
    this.carousel.nativeElement.scrollBy({
      left: 300,
      behavior: 'smooth',
    });
  }

  constructor(private blockUserService:BlockUserService, private cdr:ChangeDetectorRef){}

  ngOnInit(){
    this.blockUserService.getAllUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.cdr.detectChanges();
      }
    })
  }

  openBlockNote(userId: number, userBlocked:boolean) {
    this.selectedUserId = userId;
    this.selectedUserBlocked = userBlocked;
    this.isBlockNoteOpen = true;
  }

  closeBlockNote() {
    this.isBlockNoteOpen = false;
  }

  blockUser(dto: BlockUserDTO) {
  this.blockUserService.blockUser(dto).subscribe({
    next: (updatedUser) => {
      this.users = this.users.map(u => u.id === updatedUser.id ? updatedUser : u);
      this.closeBlockNote();
      this.cdr.detectChanges();
    }
  });
}
}
