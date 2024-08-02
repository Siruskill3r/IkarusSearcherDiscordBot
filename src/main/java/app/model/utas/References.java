package app.model.utas;

import java.util.Map;


public class References {
    private Map<String,Trip> trips;
    private Map<String,Route> routes;

    public Map<String, Trip> getTripsMap() {
        return trips;
    }

    public Map<String, Route> getRoutesMap() {
        return routes;
    }
}
