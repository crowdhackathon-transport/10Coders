package com.gkcreate.athenstour;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gkcreate.athenstour.model.TourList;
import com.gkcreate.athenstour.model.TourStep;
import com.gkcreate.athenstour.model.data.DataHelper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;



public class ActTourDetails extends FragmentActivity {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_tour_details);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.tourStepList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(new TourStepItemsAdapter(getTourSteps()));

        setUpMap();

    }

    void setUpMap(){

        if(StaticTools.currentTourItem!=null) {

            ArrayList<LatLng> directionPoints = new ArrayList<LatLng>();

            for (int i=0;i<StaticTools.currentTourItem.getSteps().size();i++) {
                directionPoints.add(new LatLng(StaticTools.currentTourItem.getSteps().get(i).getLat(), StaticTools.currentTourItem.getSteps().get(i).getLng()));
            }

            PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.BLUE);
            for (int i = 0; i < directionPoints.size(); i++) {
                rectLine.add(directionPoints.get(i));
            }
            map.addPolyline(rectLine);

            if (directionPoints != null && directionPoints.size() > 0) {
                CameraUpdate center = CameraUpdateFactory.newLatLng(directionPoints.get(0));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                map.moveCamera(center);
                map.animateCamera(zoom);
            }
        }
    }

    private ArrayList<TourStep> getTourSteps(){
        ArrayList<TourStep> steps = new ArrayList<>();
        TourStep item = new TourStep();
        item.setName("Monastiraki square");
        steps.add(item);
        item = new TourStep();
        item.setName("Ermou street");
        steps.add(item);
        item = new TourStep();
        item.setName("Monastiraki-Syntagma");
        item.setType(TourStep.TourStepType.PUBLIC_TRANSPORT);
        steps.add(item);
        item = new TourStep();
        item.setName("National Garden");
        steps.add(item);
        item = new TourStep();
        item.setName("Syntagma - Faliro");
        item.setType(TourStep.TourStepType.PUBLIC_TRANSPORT);
        steps.add(item);

        //return steps;

        TourList tourList = DataHelper.getTourList(this);
        if(tourList!=null) {
            StaticTools.currentTourItem = tourList.getData().get(0);
            return tourList.getData().get(0).getSteps();
        }
        else{
            return steps;
        }
    }

    public class TourStepItemViewHolder extends RecyclerView.ViewHolder {

        protected TextView titleText,indexText;
        protected CardView card;

        public TourStepItemViewHolder(View itemView) {
            super(itemView);
            indexText = (TextView) itemView.findViewById(R.id.txtStepIndex);
            titleText = (TextView) itemView.findViewById(R.id.txtStepName);
            card = (CardView) itemView;
        }
    }

    class TourStepItemsAdapter extends RecyclerView.Adapter<TourStepItemViewHolder>{

        private List<TourStep> tourStepItemList;

        public TourStepItemsAdapter(List<TourStep> items) {
            this.tourStepItemList = new ArrayList<TourStep>();
            this.tourStepItemList.addAll(items);
        }

        @Override
        public TourStepItemViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.item_card_tour_step, viewGroup, false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });

            return new TourStepItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(TourStepItemViewHolder holder, int i) {
            TourStep item = tourStepItemList.get(i);
            holder.titleText.setText(item.getName());
            holder.indexText.setText((i+1)+"");

            if(item.getType()== TourStep.TourStepType.PUBLIC_TRANSPORT){
                holder.card.findViewById(R.id.backView).setBackgroundColor(getResources().getColor(R.color.blue));
                holder.indexText.setBackgroundColor(getResources().getColor(R.color.blue));
            }
        }

        @Override
        public int getItemCount() {
            return tourStepItemList.size();
        }

    }


}