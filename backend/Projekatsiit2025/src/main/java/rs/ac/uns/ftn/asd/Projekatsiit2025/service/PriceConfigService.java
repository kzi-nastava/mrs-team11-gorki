package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.PriceConfig;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.VehicleType;

@Service
public class PriceConfigService {
	private PriceConfig priceConfig;

    public PriceConfigService() {
        this.priceConfig = new PriceConfig(200, 1000, 500, 120);
    }

    public PriceConfig getCurrentConfig() {
        return priceConfig;
    }

    public void updateConfig(PriceConfig newConfig) {
        this.priceConfig = newConfig;
    }
    
    public long calculatePrice(VehicleType vehicleType, double distanceKm) {
        double basePrice = switch(vehicleType) {
            case STANDARD -> priceConfig.getPriceForStandardVehicles();
            case LUXURY -> priceConfig.getPriceForLuxuryVehicles();
            case VAN -> priceConfig.getPriceForVans();
            default -> throw new IllegalArgumentException("Unknown vehicle type");
        };
        return Math.round(basePrice + distanceKm * priceConfig.getPricePerKm());
    }
}
