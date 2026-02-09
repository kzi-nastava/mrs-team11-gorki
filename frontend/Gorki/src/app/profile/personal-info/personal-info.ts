import { Component } from '@angular/core';
import { ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UserProfileService } from '../../service/user-profile-service';
import { GetUserDTO } from '../../model/ui/get-user-dto';
import { AuthService } from '../../infrastructure/auth.service';
import { UpdatedUserDTO } from '../../model/ui/updated-user-dto';
import { UpdateUserDTO } from '../../model/ui/update-user-dto';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-personal-info',
  imports: [FormsModule, CommonModule],
  templateUrl: './personal-info.html',
  styleUrl: './personal-info.css',
  standalone: true
})
export class PersonalInfo {
  imagePreview: string | ArrayBuffer | null = null;
  user!: GetUserDTO;
  originalUser!:GetUserDTO;

  constructor(private cdr: ChangeDetectorRef, private userProfileService:UserProfileService, private authService: AuthService) {}

  ngOnInit(){
    this.userProfileService.getUserInfo(this.authService.getId()).subscribe({
      next: (user) => {
        this.user = user;
        this.originalUser = structuredClone(user);
      }
    });
  }

  saveChanges(): void {
  const dto: UpdateUserDTO = {
    email: this.user.email,
    password: '', // ili null ako backend dozvoljava
    firstName: this.user.firstName,
    lastName: this.user.lastName,
    phoneNumber: Number(this.user.phoneNumber),
    address: this.user.address,
    profileImage: (this.imagePreview as string) || this.user.profileImage
  };

  this.userProfileService.updateUserInfo(this.authService.getId(), dto).subscribe({
    next: (updated: UpdatedUserDTO) => {
      this.user = {
        ...this.user,
        ...updated
      };
      this.originalUser = structuredClone(this.user);
      alert("Changes saved successfully.");
      console.log('User updated successfully');
    },
    error: (err) => {
      console.error('Update failed', err);
    }
  });
}

  revertChanges(){
    this.user = structuredClone(this.originalUser);
  }

  onImageSelected(event: Event) {
    const input = event.target as HTMLInputElement;

    if (!input.files || input.files.length === 0) {
      return;
    }

    const file = input.files[0];

    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result;
      this.cdr.detectChanges();
    };

    reader.readAsDataURL(file);
  }
}
