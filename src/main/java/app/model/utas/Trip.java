package app.model.utas;

import java.util.Objects;

public class Trip {

    private String id;
    private String tripHeadsign;
    private String tripShortName;

    public Trip() {}

    public Trip(String id, String tripHeadsign, String tripShortName) {
        this.id = id;
        this.tripHeadsign = tripHeadsign;
        this.tripShortName = tripShortName;
    }

    public String getId() {
        return id;
    }

    public String getTripHeadsign() {
        return tripHeadsign;
    }

    public String getTripShortName() {
        return tripShortName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Objects.equals(id, trip.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
