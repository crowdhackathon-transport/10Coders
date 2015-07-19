package com.gkcreate.athenstour.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MmmList {

    @JsonProperty("buses")
    private List<MmmItem> mmms;

    public List<MmmItem> getMmms() {
        return mmms;
    }

    public void setMmms(List<MmmItem> mmms) {
        this.mmms = mmms;
    }
}
