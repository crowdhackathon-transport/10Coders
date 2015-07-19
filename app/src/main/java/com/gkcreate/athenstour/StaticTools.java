package com.gkcreate.athenstour;

import com.gkcreate.athenstour.model.Poi;
import com.gkcreate.athenstour.model.PoiList;
import com.gkcreate.athenstour.model.TourItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StaticTools {

    public static TourItem currentTourItem;
    public static Poi currentPoi;

    public static Map<Integer, Boolean> selectedMmm = new HashMap<Integer, Boolean>();

    public static  List<String> mmmNamesList = new ArrayList<String>();

    public static void setMapPoints(PoiList list,GoogleMap map){

        for (int i = 0; i < list.getPois().size(); i++) {

            LatLng pos = new LatLng(Double.parseDouble(list.getPois().get(i).getLat()), Double.parseDouble(list.getPois().get(i).getLon()));
            map.addMarker(new MarkerOptions().position(pos).title(list.getPois().get(i).getName()));
        }

    }

}
