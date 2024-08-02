package app.model.utas;

import java.util.Objects;

public class Vehicle {
    private String vehicleId;
    private String licensePlate;
    private String routeId;
    private String tripId;
    private String model;
    private boolean wheelchairAccessible;


    public String getVehicleId() {
        return vehicleId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getTripId() {
        return tripId;
    }

    public String getModel() {
        return model;
    }

    public boolean isWheelchairAccessible() {
        return wheelchairAccessible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(vehicleId, vehicle.vehicleId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(vehicleId);
    }
}
