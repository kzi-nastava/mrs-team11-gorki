import { Component, EventEmitter, Output } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { CommonModule } from '@angular/common';
import { map, startWith } from 'rxjs/operators';

@Component({
  selector: 'app-person-filter',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './person-filter.html',
  styleUrl: './person-filter.css'
})
export class PersonFilter {

  @Output() personChanged = new EventEmitter<{ person: string | null }>();

  personControl = new FormControl('');
  
  personOptions = [
    'Ivan Ivić',
    'Ana Anić',
    'Marko Marković',
    'Jovana Jovanović',
    'Petar Petrović'
  ];

  filteredOptions = this.personControl.valueChanges.pipe(
    startWith(''),
    map(value => this._filter(value || ''))
  );

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.personOptions.filter(option =>
      option.toLowerCase().includes(filterValue)
    );
  }

  onSelection(value: string) {
    this.personChanged.emit({ person: value });
  }

  clear() {
    this.personControl.setValue('');
    this.personChanged.emit({ person: null });
  }
}
