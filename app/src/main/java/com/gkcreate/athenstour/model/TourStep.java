package com.gkcreate.athenstour.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TourStep {

    public enum TourStepType{
        PUBLIC_TRANSPORT,
        ON_FEET
    }

    @JsonProperty
    private String name;
    @JsonProperty
    private String index;
    @JsonProperty
    private double lat;
    @JsonProperty
    private double lng;
    @JsonProperty
    private String details;

    @JsonProperty
    private int typeNum;

    private TourStepType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public TourStepType getType() {
        return type;
    }

    public void setType(TourStepType type) {
        this.type = type;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
