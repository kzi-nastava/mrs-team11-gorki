package ftn.mrs_team11_gorki.dto;

import java.util.List;

// OSRM response: routes[0].geometry/duration/distance
public class OsrmRouteResponse {
    public List<Route> routes;

    public static class Route {
        public String geometry;   // encoded polyline (polyline6)
        public double duration;   // seconds
        public double distance;   // meters
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

}
