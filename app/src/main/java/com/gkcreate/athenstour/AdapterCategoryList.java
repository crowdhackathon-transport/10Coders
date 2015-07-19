package com.gkcreate.athenstour;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gkcreate.athenstour.model.PoiList;
import com.gkcreate.athenstour.model.data.DataHelper;

public class AdapterCategoryList extends RecyclerView.Adapter<AdapterCategoryList.ViewHolder> {
    private PoiList mPoiList;

    private int category;

    public Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextViewName;
        public TextView mTextViewDistance;
        public ImageView imageListIcon;
        public View view;
        public View.OnClickListener clickListener;


        public ViewHolder(View v) {
            super(v);
            view = v;
            mTextViewName = (TextView) v.findViewById(R.id.text_name);
            mTextViewDistance = (TextView) v.findViewById(R.id.text_distance);
            imageListIcon = (ImageView) v.findViewById(R.id.listItemIcon);
        }

    }


    public void setCategory(int category) {
        this.category = category;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterCategoryList(PoiList dataset) {
        mPoiList = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterCategoryList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_category_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        vh.view.setOnClickListener(vh.clickListener);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextViewName.setText(mPoiList.getPois().get(position).getName());
        switch (category) {
            case DataHelper.CATEGORY_MUSEUMS:
                holder.imageListIcon.setImageResource(R.drawable.museum);
                break;
            case DataHelper.CATEGORY_ART_LIFE:
                holder.imageListIcon.setImageResource(R.drawable.theater);
                break;
            case DataHelper.CATEGORY_BEACHES:
                holder.imageListIcon.setImageResource(R.drawable.beaches);
                break;
            case DataHelper.CATEGORY_SIGHTSEEING:
                holder.imageListIcon.setImageResource(R.drawable.sights);
                break;
            case DataHelper.CATEGORY_THEATERS:
                holder.imageListIcon.setImageResource(R.drawable.arts);
                break;
            case DataHelper.CATEGORY_FOOD:
                holder.imageListIcon.setImageResource(R.drawable.restaurant);
                break;
        }

        holder.view.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticTools.currentPoi = mPoiList.getPois().get(position);
                context.startActivity(new Intent(context,ActDetails.class));
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mPoiList.getPois().size();
    }
}