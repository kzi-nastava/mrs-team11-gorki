import { Component } from '@angular/core';
import { MatRadioModule } from '@angular/material/radio';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { NgClass, NgIf } from "@angular/common";
import { ReactiveFormsModule, FormArray, FormControl, FormGroup } from '@angular/forms';
import { RouterLink } from "@angular/router";
import { OrderingConfirmation } from '../ordering-confirmation/ordering-confirmation';

@Component({
  selector: 'app-ride-ordering',
  imports: [MatRadioModule, MatFormFieldModule, MatSelectModule, NgClass, ReactiveFormsModule, RouterLink, OrderingConfirmation],
  templateUrl: './ride-ordering.html',
  styleUrl: './ride-ordering.css',
})
export class RideOrdering {
  activeForm: 'main' | 'stopping' | 'passengers' = 'main';
  stoppingPoints: string[] = [''];
  stoppingPointsForm = new FormGroup({
    points: new FormArray([ new FormControl('') ])
  });
  otherPassengersForm = new FormGroup({
    passengers: new FormArray([new FormControl('')])
  });
  isModalOpen: boolean = false;

  openModal() {
    this.isModalOpen = true;
  }

  get points(): FormControl[] {
    return (this.stoppingPointsForm.get('points') as FormArray).controls as FormControl[];
  }

  get pointss(): FormArray {
    return this.stoppingPointsForm.get('points') as FormArray;
  }

  get passengers(): FormControl[] {
    return (this.otherPassengersForm.get('passengers') as FormArray).controls as FormControl[];
  }

  get passengerss(): FormArray{
    return this.otherPassengersForm.get('passengers') as FormArray;
  }

  addStoppingPoint() {
    this.pointss.push(new FormControl(''));
  }

  removeStoppingPoint(index: number) {
    this.pointss.removeAt(index);
  }

  addPassenger() {
    this.passengerss.push(new FormControl(''));
  }

  removePassenger(index: number) {
    this.passengerss.removeAt(index);
  }

  openStoppingForm() {
    this.activeForm = 'stopping';
  }

  openPassengersForm() {
    this.activeForm = 'passengers';
  }

  backToMain() {
    this.activeForm = 'main';
  }
}
