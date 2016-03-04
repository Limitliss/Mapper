package io.unearthing.mapper.ui;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.unearthing.mapper.R;
import io.unearthing.mapper.model.Trip;
import io.unearthing.mapper.model.TripSummary;

/**
 * Created by billybonks on 27/2/16.
 */
public class TripAdapter extends RecyclerView.Adapter {

    private List<TripSummary> mTrips;
    private Activity mActivity;
    public static class TripViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TripView mCard;
        Activity mParent;

        public TripViewHolder(View v, Activity parent) {
            super(v);
            mCard = new TripView(v, parent);
        }
    }

    public TripAdapter(List<TripSummary> trips,Activity activity) {
        mTrips = trips;
        mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_list_item, parent, false);
        TripViewHolder vh = new TripViewHolder(v, mActivity);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TripViewHolder tHolder = (TripViewHolder) holder;
        tHolder.mCard.populate(mTrips.get(position));
    }

    @Override
    public int getItemCount() {
        return mTrips.size();
    }
}
