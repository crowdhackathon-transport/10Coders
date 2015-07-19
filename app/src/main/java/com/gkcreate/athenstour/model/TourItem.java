package com.gkcreate.athenstour.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TourItem {

    @JsonProperty("name")
    private String name;

    @JsonProperty("centerLat")
    private double centerLat;

    @JsonProperty("centerLong")
    private double
    centerLong;

    @JsonProperty("steps")
    private ArrayList<TourStep> steps;

    public double getCenterLat() {
        return centerLat;
    }

    public double getCenterLong() {
        return centerLong;
    }

    public String getName() {
        return name;
    }

    public void setCenterLat(double centerLat) {
        this.centerLat = centerLat;
    }

    public void setCenterLong(double centerLong) {
        this.centerLong = centerLong;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<TourStep> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<TourStep> steps) {
        this.steps = steps;
    }
}
