package br.com.alteia.microservicechangeit.entities;

import java.io.Serializable;
import java.util.Objects;

import static br.com.alteia.microservicechangeit.use_cases.common.utils.EntityFieldsValidations.minLength;
import static br.com.alteia.microservicechangeit.use_cases.common.utils.EntityFieldsValidations.notNull;

public class AddressDistance implements Serializable {

    private String destination;
    private String origin;
    private String distance;
    private String duration;

    protected AddressDistance() {
    }

    public AddressDistance(String destination, String origin, String distance, String duration) {
        setDestination(destination);
        setOrigin(origin);
        setDistance(distance);
        setDuration(duration);
    }

    public String getDestination() {
        return destination;
    }

    private void setDestination(String destination) {
        minLength("destination", destination, 1);
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    private void setOrigin(String origin) {
        minLength("origin", origin, 1);
        this.origin = origin;
    }

    public String getDistance() {
        return distance;
    }

    private void setDistance(String distance) {
        notNull("distance", distance);
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    private void setDuration(String duration) {
        notNull("duration", duration);
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressDistance that = (AddressDistance) o;
        return Objects.equals(destination, that.destination) && Objects.equals(origin, that.origin) && Objects.equals(distance, that.distance) && Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destination, origin, distance, duration);
    }

    @Override
    public String toString() {
        return "AddressDistance{" +
                "destination='" + destination + '\'' +
                ", origin='" + origin + '\'' +
                ", distance=" + distance +
                ", duration=" + duration +
                '}';
    }
}