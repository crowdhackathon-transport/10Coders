package com.gkcreate.athenstour;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gkcreate.athenstour.model.MmmItem;
import com.gkcreate.athenstour.model.MmmList;

public class AdapterMmmList extends RecyclerView.Adapter<AdapterMmmList.ViewHolder> {
    private MmmList mMmmList;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextViewName;
        public TextView mTextViewDistance;
        public TextView mTextShortName;
        public ImageView imageListIcon;
        public RelativeLayout mRelativeLayout;
        public View view;
        public View.OnClickListener clickListener;
        public Context context;

        public ViewHolder(View v) {
            super(v);
            view = v;
            mTextViewName = (TextView) v.findViewById(R.id.text_name);
            mTextViewDistance = (TextView) v.findViewById(R.id.text_distance);
            mTextShortName = (TextView) v.findViewById(R.id.text_shortname);
            imageListIcon = (ImageView) v.findViewById(R.id.listItemIcon);
            mRelativeLayout = (RelativeLayout) v.findViewById(R.id.layoutmain);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterMmmList(MmmList dataset) {
        mMmmList = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterMmmList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_mmm_item, parent, false);
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
        holder.mTextViewName.setText(mMmmList.getMmms().get(position).getName());
        holder.mTextShortName.setText(mMmmList.getMmms().get(position).getShortName());
        if (mMmmList.getMmms().get(position).getBusColor() != null && mMmmList.getMmms().get(position).getBusColor().length() > 0) {
            holder.mRelativeLayout.setBackgroundColor(Color.parseColor("#" + mMmmList.getMmms().get(position).getBusColor()));
        }
        if (mMmmList.getMmms().get(position).getTextColor() != null && mMmmList.getMmms().get(position).getTextColor().length() > 2) {
            holder.mTextViewName.setTextColor(Color.parseColor("#" + mMmmList.getMmms().get(position).getTextColor()));
            holder.mTextShortName.setTextColor(Color.parseColor("#" + mMmmList.getMmms().get(position).getTextColor()));
        } else {
            holder.mTextViewName.setTextColor(Color.BLACK);
            holder.mTextShortName.setTextColor(Color.BLACK);
        }

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StaticTools.selectedMmm.get(position) == null || !StaticTools.selectedMmm.get(position)) {
                    StaticTools.selectedMmm.put(position, true);
                    mMmmList.getMmms().get(position).setIsSelected(true);
                } else {
                    StaticTools.selectedMmm.put(position, false);
                    mMmmList.getMmms().get(position).setIsSelected(false);
                }
                notifyItemChanged(position);
            }
        });

        if (StaticTools.selectedMmm.get(position) == null || !StaticTools.selectedMmm.get(position)) {
            holder.imageListIcon.setImageResource(R.drawable.logo_only_small_trans);
            mMmmList.getMmms().get(position).setIsSelected(false);
        } else {
            holder.imageListIcon.setImageResource(R.drawable.logo_only_small);
            mMmmList.getMmms().get(position).setIsSelected(true);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mMmmList.getMmms().size();
    }

    public MmmList getItems() {

        int i = 0;
        for (MmmItem item : mMmmList.getMmms()) {
            if (StaticTools.selectedMmm.get(i) != null && StaticTools.selectedMmm.get(i)) {
                mMmmList.getMmms().get(i).setIsSelected(true);
            }
            i++;
        }

        return mMmmList;
    }
}