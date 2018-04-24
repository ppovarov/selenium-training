package ru.stqa.training.selenium.model;

import java.util.Objects;

public class Zone {
    private int id;
    private String code;
    private String name;

    public int getID() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    public Zone withID(int id) {
        this.id = id;
        return this;
    }

    public Zone withCode(String code) {
        this.code = code;
        return this;
    }

    public Zone withName(String name) {
        this.name = name;
        return this;
    }


    @Override
    public String toString() {
        return "Zone{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
