package com.gkcreate.athenstour.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TourList {

    @JsonProperty("data")
    private ArrayList<TourItem> data;

    public ArrayList<TourItem> getData() {
        return data;
    }

    public void setData(ArrayList<TourItem> data) {
        this.data = data;
    }
}
