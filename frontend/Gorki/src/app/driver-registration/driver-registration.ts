import { Component, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {MatRadioModule} from '@angular/material/radio';
import {MatSelectModule} from '@angular/material/select';
import {MatFormFieldModule} from '@angular/material/form-field';
import { NgClass, NgIf } from '@angular/common';
import { ReactiveFormsModule } from "@angular/forms";
import { CreateDriverDTO } from '../model/ui/create-driver-dto';
import { CreateUserDTO } from '../model/ui/create-user-dto';
import { CreateVehicleDTO } from '../model/ui/create-vehicle-dto';
import { DriverRegistrationService } from '../service/driver-registration-service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DEFAULT_PROFILE_PIC } from '../../env/profile-pic';

type VehicleType = 'STANDARD' | 'LUXURY' | 'VAN';

@Component({
  selector: 'app-driver-registration',
  imports: [MatRadioModule, MatFormFieldModule, MatSelectModule, NgClass, ReactiveFormsModule, NgIf],
  templateUrl: './driver-registration.html',
  styleUrl: './driver-registration.css',
})
export class DriverRegistration {
  imagePreview: string | ArrayBuffer | null = null;
  firstStep: boolean = true;
  form!: FormGroup;

  constructor(private cdr: ChangeDetectorRef, private fb:FormBuilder, private driverRegistrationService:DriverRegistrationService, private snackbar:MatSnackBar ) {}

  ngOnInit(){
    this.form = this.fb.group({
      firstNameInput: ['', [Validators.required]],
      lastNameInput: ['', [Validators.required]],
      homeAddressInput: ['', [Validators.required]],
      phoneNumberInput: ['', [Validators.required]],
      emailInput: ['', [Validators.required, Validators.email]],

      modelInput: ['', [Validators.required]],
      typeSelect: ['STANDARD' as VehicleType, [Validators.required]],
      licensePlateInput: ['', [Validators.required]],
      seatsInput: [0, [Validators.required]],
      babyRadio: [false, [Validators.required]],
      petRadio: [false, [Validators.required]],
    });
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

  moveToSecondStep(){
    const step1Controls = ['firstNameInput','lastNameInput','homeAddressInput','phoneNumberInput','emailInput'] as const;
    step1Controls.forEach(c => this.form.get(c)?.markAsTouched());

    const step1Valid = step1Controls.every(c => this.form.get(c)?.valid);
    if (!step1Valid) {
      this.snackbar.open("You must fill out all inputs", "Close", {duration:4000})
      return;
    }

    this.firstStep = false;
  }

  backToFirstStep(){
    this.firstStep = true;
  }

  submit(){
    this.form.markAllAsTouched();
    if (this.form.invalid){
      this.snackbar.open("You must fill out all inputs", "Close", {duration:4000})
      return;
    } 

    const v = this.form.getRawValue();

    const user:CreateUserDTO = {
      email: v.emailInput!,
      password: '',
      firstName: v.firstNameInput!,
      lastName: v.lastNameInput!,
      phoneNumber: Number(v.phoneNumberInput),
      address: v.homeAddressInput!,
      profileImage: this.imagePreview ? String(this.imagePreview) : DEFAULT_PROFILE_PIC
    }

    const vehicle:CreateVehicleDTO = {
      model: v.modelInput!,
      plateNumber: v.licensePlateInput!,
      seats: Number(v.seatsInput!),
      type: v.typeSelect!,
      babyTransport: v.babyRadio!,
      petFriendly: v.petRadio!
    }

    const dto:CreateDriverDTO = {
      user: user,
      vehicle: vehicle
    }

    this.driverRegistrationService.createDriver(dto).subscribe({
      next: () =>{
        this.snackbar.open("Driver created successfully!", "Close", {duration:4000});
        this.form.reset({
          typeSelect: 'STANDARD',
          babyRadio: false,
          petRadio: false,
          seatsInput: 0
        });
        this.firstStep = true;
      },
      error: (err) => {
        this.snackbar.open("There was a problem in driver registration.", "Close", {duration:4000});
        console.log(err);
      }
    });
    
  }

}
