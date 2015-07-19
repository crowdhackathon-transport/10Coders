package com.gkcreate.athenstour.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PoiList {

    @JsonProperty("wpt")
    private List<Poi> pois;

    public List<Poi> getPois() {
        return pois;
    }

    public void setPois(List<Poi> pois) {
        this.pois = pois;
    }
}
