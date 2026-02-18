import { ChangeDetectorRef, Component, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartType } from 'chart.js';

import { DateFilter } from '../rides/filters/date-filter/date-filter';
import { ReportService } from '../service/report-service';
import { ReportDTO } from '../model/ui/report-dto';
import { finalize } from 'rxjs';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-report-admin',
  imports: [CommonModule, DateFilter, BaseChartDirective, FormsModule],
  templateUrl: './report-admin.html',
  styleUrl: './report-admin.css',
})
export class ReportAdmin {
  @ViewChild('df') df!: DateFilter;
  userEmail: string = '';

  dto: ReportDTO | null = null;
  loading = false;
  error: string | null = null;

  dailyRidesType: ChartType = 'bar';
  dailyKmType: ChartType = 'line';
  dailyMoneyType: ChartType = 'line';

  cumRidesType: ChartType = 'line';
  cumKmType: ChartType = 'line';
  cumMoneyType: ChartType = 'line';

  dailyRidesData: ChartConfiguration['data'] = { labels: [], datasets: [] };
  dailyKmData: ChartConfiguration['data'] = { labels: [], datasets: [] };
  dailyMoneyData: ChartConfiguration['data'] = { labels: [], datasets: [] };

  cumRidesData: ChartConfiguration['data'] = { labels: [], datasets: [] };
  cumKmData: ChartConfiguration['data'] = { labels: [], datasets: [] };
  cumMoneyData: ChartConfiguration['data'] = { labels: [], datasets: [] };

  commonOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
  };

  dailyRidesOptions = this.commonOptions;
  dailyKmOptions = this.commonOptions;
  dailyMoneyOptions = this.commonOptions;

  cumRidesOptions = this.commonOptions;
  cumKmOptions = this.commonOptions;
  cumMoneyOptions = this.commonOptions;

  constructor(
    private reportService: ReportService,
    private cdr: ChangeDetectorRef
  ) {}

  generate() {
    const from = this.df?.fromDate ?? null;
    const to = this.df?.toDate ?? null;

    if (from && to && to < from) {
      this.error = 'Invalid date range: "To" must be after "From".';
      return;
    }

    this.loading = true;
    this.error = null;

    this.reportService.generateAggregateReport(from, to).pipe(
      finalize(() => {
        this.loading = false;
        this.cdr.detectChanges();
      })
    ).subscribe({
      next: (dto) => {
        this.dto = dto;
        this.setCharts(dto);
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message ?? 'Failed to load report.';
      },
    });
  }

  generateForUser(){
    const email = (this.userEmail || '').trim();
    if (!email) {
      this.error = 'Please enter a user email.';
      return;
    }
    const from = this.df?.fromDate ?? null;
    const to = this.df?.toDate ?? null;

    if (from && to && to < from) {
      this.error = 'Invalid date range: "To" must be after "From".';
      return;
    }

    this.loading = true;
    this.error = null;

    this.reportService.generateAdminUserReport( email, from, to).pipe(
      finalize(() => {
        this.loading = false;
        this.cdr.detectChanges();
      })
    ).subscribe({
      next: (dto) => {
        this.dto = dto;
        this.setCharts(dto);
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message ?? 'Failed to load report for user.';
      },
    });
  }

  private setCharts(dto: ReportDTO) {
    const labels = dto.points.map(p => p.date);

    this.dailyRidesData = {
      labels,
      datasets: [
        { label: 'Rides per day', data: dto.points.map(p => p.rideCount) },
      ],
    };

    this.dailyKmData = {
      labels,
      datasets: [
        { label: 'Km per day', data: dto.points.map(p => p.kilometers) },
      ],
    };

    this.dailyMoneyData = {
      labels,
      datasets: [
        { label: 'Money per day', data: dto.points.map(p => p.money) },
      ],
    };

    this.cumRidesData = {
      labels,
      datasets: [
        { label: 'Cumulative rides', data: dto.points.map(p => p.cumulativeRideCount) },
      ],
    };

    this.cumKmData = {
      labels,
      datasets: [
        { label: 'Cumulative km', data: dto.points.map(p => p.cumulativeKilometers) },
      ],
    };

    this.cumMoneyData = {
      labels,
      datasets: [
        { label: 'Cumulative money', data: dto.points.map(p => p.cumulativeMoney) },
      ],
    };
  }
}
