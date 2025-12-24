import { Component, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-sort-filter',
  standalone: true,
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatSelectModule
  ],
  templateUrl: './sort-filter.html',
  styleUrls: ['./sort-filter.css']
})
export class SortFilter {
  selectedSort: string | null = null;
  selectedOrder:string|null=null;

  @Output() sortChanged = new EventEmitter<{criteria: string, order: 'asc'|'desc'}>();

  sortOptions = [
    { value: 'startTime', label: 'Starting time' },
    { value: 'endTime', label: 'Ending time' },
    { value: 'startLocation', label: 'Starting point' },
    { value: 'destination', label: 'Destination' },
    { value: 'price', label: 'Price' }
  ];

  orderOptions =[
    {value:'asc', label:'Ascending'},
    {value:'desc', label:'Descending'}
  ]

  onSortChange() {
    if (this.selectedSort && this.selectedOrder) {
      this.sortChanged.emit({ criteria: this.selectedSort, order: this.selectedOrder as 'asc'|'desc' });
    }
  }
}