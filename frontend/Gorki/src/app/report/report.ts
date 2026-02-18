import { ChangeDetectorRef, Component, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartType } from 'chart.js';

import { DateFilter } from '../rides/filters/date-filter/date-filter';
import { ReportService } from '../service/report-service';
import { AuthService } from '../infrastructure/auth.service';
import { ReportDTO } from '../model/ui/report-dto';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-report',
  standalone: true,
  imports: [CommonModule, DateFilter, BaseChartDirective],
  templateUrl: './report.html',
  styleUrl: './report.css',
})
export class Report {
  @ViewChild('df') df!: DateFilter;

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
    private auth: AuthService,
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

    const role = this.auth.getRole() ?? null;

    let obs;
    if (role === 'DRIVER' || role === 'ROLE_DRIVER') {
      obs = this.reportService.generateDriverReport(from, to);
    } else {
      obs = this.reportService.generatePassengerReport(from, to);
    }

    obs.pipe(
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
