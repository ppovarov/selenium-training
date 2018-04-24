package ru.stqa.training.selenium.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Country {
    private int id;
    private String code;
    private String name;
    private int zonesCount;
    private List<Zone> zones = new ArrayList<Zone>();


    public int getID() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getZonesCount() {
        return zonesCount;
    }

    public List<Zone> getZones() {
        return zones;
    }

    public Country withID(int id) {
        this.id = id;
        return this;
    }

    public Country withCode(String code) {
        this.code = code;
        return this;
    }

    public Country withName(String name) {
        this.name = name;
        return this;
    }

    public Country withZonesCount(int zones) {
        this.zonesCount = zones;
        return this;
    }

    public Country setZones(List<Zone> zones) {
        this.zones = zones;
        return this;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", zonesCount=" + zonesCount +
                ", zones=" + zones +
                '}'+'\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return id == country.id &&
                zonesCount == country.zonesCount &&
                Objects.equals(code, country.code) &&
                Objects.equals(name, country.name) &&
                Objects.equals(zones, country.zones);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, code, name, zonesCount, zones);
    }
}