package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.report;

import java.time.LocalDate;

public class ReportPointDTO {
	private LocalDate date;
	private int rideCount;
	private double kilometers;
	private double money;
	private int cumulativeRideCount;
    private double cumulativeKilometers;
    private double cumulativeMoney;
	public ReportPointDTO() {
		super();
	}
	public ReportPointDTO(LocalDate date, int rideCount, double kilometers, double money, int cumulativeRideCount,
			double cumulativeKilometers, double cumulativeMoney) {
		super();
		this.date = date;
		this.rideCount = rideCount;
		this.kilometers = kilometers;
		this.money = money;
		this.cumulativeRideCount = cumulativeRideCount;
		this.cumulativeKilometers = cumulativeKilometers;
		this.cumulativeMoney = cumulativeMoney;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public int getRideCount() {
		return rideCount;
	}
	public void setRideCount(int rideCount) {
		this.rideCount = rideCount;
	}
	public double getKilometers() {
		return kilometers;
	}
	public void setKilometers(double kilometers) {
		this.kilometers = kilometers;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public int getCumulativeRideCount() {
		return cumulativeRideCount;
	}
	public void setCumulativeRideCount(int cumulativeRideCount) {
		this.cumulativeRideCount = cumulativeRideCount;
	}
	public double getCumulativeKilometers() {
		return cumulativeKilometers;
	}
	public void setCumulativeKilometers(double cumulativeKilometers) {
		this.cumulativeKilometers = cumulativeKilometers;
	}
	public double getCumulativeMoney() {
		return cumulativeMoney;
	}
	public void setCumulativeMoney(double cumulativeMoney) {
		this.cumulativeMoney = cumulativeMoney;
	}
	
}
