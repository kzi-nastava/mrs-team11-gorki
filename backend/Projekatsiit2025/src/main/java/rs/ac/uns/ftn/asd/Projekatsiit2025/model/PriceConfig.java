package rs.ac.uns.ftn.asd.Projekatsiit2025.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class PriceConfig {
	private double priceForStandardVehicles;
	private double priceForLuxuryVehicles;
	private double priceForVans;
	private double pricePerKm;
	
	public PriceConfig() {
	}

	public PriceConfig(double priceForStandardVehicles, double priceForLuxuryVehicles, double priceForVans,
			double pricePerKm) {
		super();
		this.priceForStandardVehicles = priceForStandardVehicles;
		this.priceForLuxuryVehicles = priceForLuxuryVehicles;
		this.priceForVans = priceForVans;
		this.pricePerKm = pricePerKm;
	}

	public double getPriceForStandardVehicles() {
		return priceForStandardVehicles;
	}

	public void setPriceForStandardVehicles(double priceForStandardVehicles) {
		this.priceForStandardVehicles = priceForStandardVehicles;
	}

	public double getPriceForLuxuryVehicles() {
		return priceForLuxuryVehicles;
	}

	public void setPriceForLuxuryVehicles(double priceForLuxuryVehicles) {
		this.priceForLuxuryVehicles = priceForLuxuryVehicles;
	}

	public double getPriceForVans() {
		return priceForVans;
	}

	public void setPriceForVans(double priceForVans) {
		this.priceForVans = priceForVans;
	}

	public double getPricePerKm() {
		return pricePerKm;
	}

	public void setPricePerKm(double pricePerKm) {
		this.pricePerKm = pricePerKm;
	}
	
	
}
