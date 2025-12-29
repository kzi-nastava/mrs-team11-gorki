package rs.ac.uns.ftn.asd.Projekatsiit2025.model;

public class PriceConfig {
	private double priceByVehicleType;
	private double pricePerKm;
	
	public PriceConfig() {
	}
	public PriceConfig(double priceByVehicleType, double pricePerKm) {
		this.priceByVehicleType = priceByVehicleType;
		this.pricePerKm = pricePerKm;
	}
	public double getPriceByVehicleType() {
		return priceByVehicleType;
	}
	public void setPriceByVehicleType(double priceByVehicleType) {
		this.priceByVehicleType = priceByVehicleType;
	}
	public double getPricePerKm() {
		return pricePerKm;
	}
	public void setPricePerKm(double pricePerKm) {
		this.pricePerKm = pricePerKm;
	}
	
	
}
