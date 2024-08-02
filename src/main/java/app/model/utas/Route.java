package app.model.utas;

import java.util.Objects;

public class Route {
    private String id;
    private String description;
    private String agencyId;

    public Route() {}

    public Route(String id, String description, String agencyId) {
        this.id = id;
        this.description = description;
        this.agencyId = agencyId;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getAgencyId() {
        return agencyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return id == route.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
