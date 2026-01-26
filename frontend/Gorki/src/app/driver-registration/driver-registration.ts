import { Component, ChangeDetectorRef } from '@angular/core';
import {MatRadioModule} from '@angular/material/radio';
import {MatSelectModule} from '@angular/material/select';
import {MatFormFieldModule} from '@angular/material/form-field';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-driver-registration',
  imports: [MatRadioModule, MatFormFieldModule, MatSelectModule, NgClass],
  templateUrl: './driver-registration.html',
  styleUrl: './driver-registration.css',
})
export class DriverRegistration {
  imagePreview: string | ArrayBuffer | null = null;
  firstStep: boolean = true;
  
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

  moveToSecondStep(){
    this.firstStep = false;
  }

  backToFirstStep(){
    this.firstStep = true;
  }

}
