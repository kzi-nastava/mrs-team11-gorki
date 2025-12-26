package rs.ac.uns.ftn.asd.Projekatsiit2025.model;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;

public class Driver extends User {
	
	private Vehicle vehicle;
	private DriverStatus status;
	private double activityLast24h;
	
	public Driver() {
		super();
	}
	public Driver(User account, Vehicle vehicle,
			DriverStatus status, double activityLast24h) {
		super();
		this.vehicle = vehicle;
		this.status = status;
		this.activityLast24h = activityLast24h;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	public DriverStatus getStatus() {
		return status;
	}
	public void setStatus(DriverStatus status) {
		this.status = status;
	}
	public double getActivityLast24h() {
		return activityLast24h;
	}
	public void setActivityLast24h(double activityLast24h) {
		this.activityLast24h = activityLast24h;
	}
	
}
