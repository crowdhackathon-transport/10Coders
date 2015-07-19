package com.gkcreate.athenstour.model.data;


import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gkcreate.athenstour.R;
import com.gkcreate.athenstour.model.MmmList;
import com.gkcreate.athenstour.model.PoiList;
import com.gkcreate.athenstour.model.TourList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataHelper {

    public static final int CATEGORY_MUSEUMS = R.integer.id_poi_museums;
    public static final int CATEGORY_SIGHTSEEING = R.integer.id_poi_sightseeing;
    public static final int CATEGORY_BEACHES = R.integer.id_poi_beaches;
    public static final int CATEGORY_THEATERS = R.integer.id_poi_theaters;
    public static final int CATEGORY_ART_LIFE = R.integer.id_poi_artlife;
    public static final int CATEGORY_FOOD = R.integer.id_poi_food;
    public static final int CATEGORY_SOUVLAKIA = R.integer.id_poi_souvlakia;


    private static PoiList getPoiList(String jsonData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonData, PoiList.class);
        } catch (Exception e) {
            Log.d("EXCEPTION", e.getMessage());
        }
        return null;
    }

    public static PoiList getMuseumsPoiList() {
        return DataHelper.getPoiList(PoiData.MUSEUM_JSON_DATA);
    }

    public static PoiList getSightseeingPoiList() {
        return DataHelper.getPoiList(PoiData.SIGHTSEEING_JSON_DATA);
    }

    public static PoiList getBeachesPoiList() {
        return DataHelper.getPoiList(PoiData.BEACHES_JSON_DATA);
    }

    public static PoiList getTheatersPoiList() {
        return DataHelper.getPoiList(PoiData.THEATERS_JSON_DATA);
    }

    public static PoiList getArtLifePoiList() {
        return DataHelper.getPoiList(PoiData.ART_LIFE_JSON_DATA);
    }

    public static PoiList getFoodPoiList() {
        return DataHelper.getPoiList(PoiData.FOOD_JSON_DATA);
    }

    public static PoiList getSouvlakiaPoiList() {
        return DataHelper.getPoiList(PoiData.SOUVLAKIA_JSON_DATA);
    }

    public static TourList getTourList(Context context){
        String jsonData = readFromfile("tours.json",context);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonData, TourList.class);
        } catch (Exception e) {
            Log.d("EXCEPTION", e.getMessage());
        }
        return null;
    }

    public static MmmList getMmmList(Context context){
        String jsonData = readFromfile("buses.json",context);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonData, MmmList.class);
        } catch (Exception e) {
            Log.d("EXCEPTION", e.getMessage());
        }
        return null;
    }

    public static String readFromfile(String fileName, Context context) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = context.getResources().getAssets()
                    .open(fileName, Context.MODE_WORLD_READABLE);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line = "";
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }
}
