package app.service;

import app.model.app.VehicleData;
import app.model.utas.*;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UtasConnector implements Runnable {

    private List<VehicleData> result = null;

    private final Location location;
    private final double latSpan;
    private final double lonSpan;
    private final Predicate<Vehicle> filter;

    public UtasConnector(Location location, double latSpan, double lonSpan, Predicate<Vehicle> filter) {
        if(location == null || latSpan < 0 || lonSpan < 0 || filter == null )
            throw new NullPointerException();
        this.location = location;
        this.latSpan = latSpan;
        this.lonSpan = lonSpan;
        this.filter = filter;
    }

    @Override
    public void run() {
     //   System.out.println(Thread.currentThread().getName() + ": UtasConnector started");
        List<Vehicle> vehicles;
        String httpsUrl = "https://utas.hu/api/query/v1/ws/otp/api/where/vehicles-for-location.json?lat=" + location.lat + "&latSpan=" + latSpan +"&lon=" + location.lon +"&lonSpan=" + lonSpan +"&key=ride-web&version=4&appVersion=3.18.0-162511-795842-71060206";
        StringBuilder response = new StringBuilder();
        Trip dummyTrip = new Trip("dummy","N/A","N/A");
        Route dummyRoute = new Route("dummy","N/A", "N/A");
        try {
            HttpURLConnection connection = null;
            URL url = new URL(httpsUrl);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader in = new BufferedReader(streamReader);

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            connection.disconnect();
            } catch (IOException e) {
            System.out.println("baj van");
            throw new RuntimeException(e);
            }

            Gson gson = new Gson();
            ResponseTop responseTop = gson.fromJson(response.toString(), ResponseTop.class);
            vehicles = responseTop.getData().getList();
          //  System.out.println("Vehicles found: " + vehicles.size());
        result = vehicles.stream().filter(filter).map(vehicle -> {
            Trip trip = responseTop.getData().getReferences().getTripsMap().get(vehicle.getTripId());
            Route route = responseTop.getData().getReferences().getRoutesMap().get(vehicle.getRouteId());
            if(trip == null) trip = dummyTrip;
            if(route == null) route = dummyRoute;
            Object[] destinationsO =  Arrays.stream(route.getDescription().split(Pattern.quote("|"))).map(String::trim).toArray();
            String[] destinations = new String[2];
            if(destinationsO.length < 2) {
                destinations[0] = "N/A";
                destinations[1] = "N/A";
            } else {
                destinations[0] = destinationsO[0].toString();
                destinations[1] = destinationsO[1].toString();
            }
            String startStation, endStation;
            if(trip.getTripHeadsign().trim().equals(destinations[0])){
                startStation = destinations[1];
                endStation = destinations[0];
            } else {
                startStation = destinations[0];
                endStation = destinations[1];
            }
       //     System.out.println(Thread.currentThread().getName() + ": UtasConnector finished");
            return new VehicleData(vehicle.getLicensePlate(),vehicle.getModel(),trip.getTripShortName(),startStation,endStation);
        }).toList();
    }


    public List<VehicleData> getResult() {
        return result;
    }

}
