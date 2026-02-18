package ftn.mrs_team11_gorki.dto;

// Nominatim: /search vraća listu objekata sa lat/lon kao string
public class NominatimPlaceDTO {
    public String lat;
    public String lon;

    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
