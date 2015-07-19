package com.gkcreate.athenstour;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.gkcreate.athenstour.model.MmmItem;
import com.gkcreate.athenstour.model.MmmList;
import com.gkcreate.athenstour.model.data.DataHelper;

public class ActMmmConfiguration extends Activity {

    private RecyclerView mRecyclerView;
    private AdapterMmmList mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mmm_configuration);

        MmmList mmmList = DataHelper.getMmmList(getApplicationContext());


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // specify an adapter (see also next example)
        mAdapter = new AdapterMmmList(mmmList);
        //mAdapter.context = this;
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MmmList items = mAdapter.getItems();
                StaticTools.mmmNamesList.clear();
                for (MmmItem mmmItem : items.getMmms()) {
                    if (mmmItem.isSelected()) {
                        String shortName = mmmItem.getShortName();
                        shortName = shortName.replace("Τ", "T");
                        shortName = shortName.replace("Μ", "M");
                        shortName = shortName.replace("Π", "P");
                        shortName = shortName.replace("Α", "A");
                        shortName = shortName.replace("Β", "B");
                        shortName = shortName.replace("Γ", "G");
                        shortName = shortName.replace("Ε", "E");
                        shortName = shortName.replace("Χ", "X");
                        StaticTools.mmmNamesList.add(shortName);
                    }
                }
                Log.d("MMM_LIST", "Size: " + StaticTools.mmmNamesList.size());
                finish();
            }
        });
    }
}