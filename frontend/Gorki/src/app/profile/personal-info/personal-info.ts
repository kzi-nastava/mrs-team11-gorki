import { Component } from '@angular/core';
import { ChangeDetectorRef } from '@angular/core';
import { User } from '../../model/ui/user';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-personal-info',
  imports: [FormsModule],
  templateUrl: './personal-info.html',
  styleUrl: './personal-info.css',
})
export class PersonalInfo {
  imagePreview: string | ArrayBuffer | null = null;
  user: User = {
    email:'marko.pavlovic2404004@gmail.com',
    firstName:'Marko',
    lastName:'Pavlovic',
    phoneNumber:381648816145,
    address:'Mornarska 51, Novi Sad',
    profileImage:'/user-pic.png',
  };

  constructor(private cdr: ChangeDetectorRef) {}

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
