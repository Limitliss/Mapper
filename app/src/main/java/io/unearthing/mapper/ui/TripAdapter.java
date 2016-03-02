package io.unearthing.mapper.ui;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.unearthing.mapper.R;
import io.unearthing.mapper.model.Trip;

/**
 * Created by billybonks on 27/2/16.
 */
public class TripAdapter extends RecyclerView.Adapter {

    private List<Trip> mTrips;

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mCard;
        public TripViewHolder(View v) {
            super(v);
            mCard = v;
        }
    }

    public TripAdapter(List<Trip> trips) {
        mTrips = trips;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_list_item, parent, false);
        TripViewHolder vh = new TripViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TripViewHolder tHolder = (TripViewHolder) holder;
        TextView txt = (TextView) tHolder.mCard.findViewById(R.id.trip_title);
        Trip trip = mTrips.get(0);
        txt.setText(trip.getTitle());
       // holder.mTextView.setText(mDataset[position]);
    }

    @Override
    public int getItemCount() {
        return mTrips.size();
    }
}
