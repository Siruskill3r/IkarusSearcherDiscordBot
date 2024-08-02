package app.service;

import app.model.app.VehicleData;
import app.model.utas.Location;
import app.model.utas.Vehicle;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UtasDataService implements Runnable {

    private static final int REFRESH_PERIOD_MINUTES = 3;

    private final Set<VehicleData> result = new HashSet<>();

    private final List<Location> locations;
    private final List<Double> latSpans;
    private final List<Double> lonSpans;
    private final Predicate<Vehicle> filter;

    private LocalDateTime lastUpdated = null;

    public UtasDataService(List<Location> location, List<Double> latSpan, List<Double> lonSpan, Predicate<Vehicle> filter) {
        if(location == null || filter == null || location.size() != latSpan.size() || location.size() != lonSpan.size() )
            throw new NullPointerException();
        this.locations = location;
        this.latSpans = latSpan;
        this.lonSpans = lonSpan;
        this.filter = filter;
    }

    public List<VehicleData> getResult(Predicate<VehicleData> filter) {
        synchronized (result) {
            return result.stream().filter(filter).sorted(Comparator.comparing(VehicleData::getModel)).toList();
        }
    }

    @Override
    public void run() {
        while(true) {
            try {
                List<Thread> threads = new ArrayList<>();
                List<UtasConnector> workers = new ArrayList<>();
                for(int i = 0; i < locations.size(); i++) {
                    UtasConnector worker = new UtasConnector(locations.get(i),latSpans.get(i),lonSpans.get(i),filter);
                    workers.add(worker);
                    Thread t = new Thread((worker));
                    threads.add(t);
                    t.start();
                }
                for(Thread t : threads) {
                    t.join();
                }
                synchronized (result) {
                    result.clear();
                    for(UtasConnector worker : workers) {
                        result.addAll(worker.getResult());
                    }
                }
                System.out.println("Data refreshed: " + (lastUpdated = LocalDateTime.now()));
                Thread.sleep(1000 * 60 * REFRESH_PERIOD_MINUTES);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
}
