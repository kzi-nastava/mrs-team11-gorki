package ftn.mrs_team11_gorki.dto;

public class PriceConfigDTO {
    private double priceForStandardVehicles;
    private double priceForLuxuryVehicles;
    private double priceForVans;
    private double pricePerKm;

    public PriceConfigDTO() {
    }

    public PriceConfigDTO(double priceForStandardVehicles, double priceForLuxuryVehicles, double priceForVans,
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
