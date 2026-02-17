import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { CommonModule } from '@angular/common';
import { map, startWith } from 'rxjs/operators';
import { AdminHistoryService, UserOptionDTO } from '../../../service/admin-history-service';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';

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
export class PersonFilter implements OnInit {

  @Output() personChanged = new EventEmitter<{ personEmail: string | null }>();

  personControl = new FormControl<string | UserOptionDTO>('');

  personOptions: UserOptionDTO[] = [];

  constructor(private adminHistoryService: AdminHistoryService) {}

  ngOnInit() {
    this.adminHistoryService.getUsers().subscribe({
      next: users => this.personOptions = users,
      error: err => console.error('Ne mogu da dohvatim korisnike', err),
    });
  }

  filteredOptions = this.personControl.valueChanges.pipe(
    startWith(''),
    map(value => {
      const text = typeof value === 'string' ? value : this.displayUser(value);
      const filterValue = text.toLowerCase();

      return this.personOptions.filter(u =>
        this.displayUser(u).toLowerCase().includes(filterValue) ||
        (u.email?.toLowerCase().includes(filterValue))
      );
    })
  );

  displayUser = (u: UserOptionDTO | null): string =>
  u ? `${u.firstName} ${u.lastName}`.trim() : '';

  onSelection(event: MatAutocompleteSelectedEvent) {
    const user = event.option.value as UserOptionDTO;
    this.personChanged.emit({ personEmail: user.email });
  }

  clear() {
    this.personControl.setValue('');
    this.personChanged.emit({ personEmail: null });
  }
}