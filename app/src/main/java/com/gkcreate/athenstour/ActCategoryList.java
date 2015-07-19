package com.gkcreate.athenstour;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gkcreate.athenstour.model.PoiList;
import com.gkcreate.athenstour.model.data.DataHelper;

public class ActCategoryList extends Activity {

    public static final String CATEGORY_DATA_TO_DISPLAY = "CATEGORY_DATA_TO_DISPLAY";

    private RecyclerView mRecyclerView;
    private AdapterCategoryList mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_category_list);

        PoiList poiList;

        switch (getIntent().getIntExtra(ActCategoryList.CATEGORY_DATA_TO_DISPLAY, DataHelper.CATEGORY_MUSEUMS)) {
            case DataHelper.CATEGORY_MUSEUMS:
                poiList = DataHelper.getMuseumsPoiList();
                break;
            case DataHelper.CATEGORY_ART_LIFE:
                poiList = DataHelper.getArtLifePoiList();
                break;
            case DataHelper.CATEGORY_BEACHES:
                poiList = DataHelper.getBeachesPoiList();
                break;
            case DataHelper.CATEGORY_SIGHTSEEING:
                poiList = DataHelper.getSightseeingPoiList();
                break;
            case DataHelper.CATEGORY_THEATERS:
                poiList = DataHelper.getTheatersPoiList();
                break;
            case DataHelper.CATEGORY_FOOD:
                poiList = DataHelper.getFoodPoiList();
                break;
            default:
                poiList = DataHelper.getMuseumsPoiList();
                break;
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // specify an adapter (see also next example)
        mAdapter = new AdapterCategoryList(poiList);
        mAdapter.context = this;
        //mAdapter.context = this;
        mAdapter.setCategory(getIntent().getIntExtra(ActCategoryList.CATEGORY_DATA_TO_DISPLAY, DataHelper.CATEGORY_MUSEUMS));
        mRecyclerView.setAdapter(mAdapter);

    }

}
