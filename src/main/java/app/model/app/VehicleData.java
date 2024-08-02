package app.model.app;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class VehicleData {
    private final String numberPlate;
    private final String model;
    private final String tripNumber;
    private final String fromStation;
    private final String toStation;
    private final String tripId;

    public VehicleData(String numberPlate, String model, String tripNumber, String fromStation, String toStation,String tripId) {
        this.numberPlate = numberPlate;
        this.model = model;
        this.tripNumber = tripNumber;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.tripId = tripId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleData that = (VehicleData) o;
        return Objects.equals(numberPlate, that.numberPlate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(numberPlate);
    }

    @Override
    public String toString() {
        return numberPlate + "(" + model + ")@" + tripNumber + ":" + fromStation + "->" + toStation + '\n' + "[To site](https://utas.hu/trip/" + tripId + "/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) +  ")";
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public String getModel() {
        return model;
    }

    public String getTripNumber() {
        return tripNumber;
    }

    public String getFromStation() {
        return fromStation;
    }

    public String getToStation() {
        return toStation;
    }
}
