package com.gkcreate.athenstour.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MmmItem {
    @JsonProperty("route_id")
    private String id;
    @JsonProperty("route_short_name")
    private String shortName;
    @JsonProperty("route_long_name")
    private String name;
    @JsonProperty("route_desc")
    private String description;
    @JsonProperty("route_text_color")
    private String textColor;
    @JsonProperty("route_color")
    private String busColor;
    @JsonProperty("route_type")
    private String busType;

    @JsonIgnore
    private boolean isSelected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getBusColor() {
        return busColor;
    }

    public void setBusColor(String busColor) {
        this.busColor = busColor;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
