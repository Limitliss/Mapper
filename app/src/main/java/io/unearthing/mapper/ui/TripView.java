package io.unearthing.mapper.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import io.unearthing.mapper.CloudentBuilder;
import io.unearthing.mapper.R;
import io.unearthing.mapper.model.Trip;
import io.unearthing.mapper.model.TripSummary;
import io.unearthing.mapper.services.UploadTrip;
import io.unearthing.mapper.ui.activities.MapActivity;

/**
 * Created by billybonks on 3/3/16.
 */
public class TripView implements View.OnClickListener {
    TripSummary mTrip;
    Activity mParent;
    List<Trip> mTrips;
    TextView mPointsLabel;
    TextView mTimeSpentLabel;
    TextView mDistanceLabel;
    TextView mSpeedLabel;
    TextView mTitle;
    ImageButton mOptions;
    View mView;

    public TripView(View view, Activity parent){
        mView = view;
        mParent = parent;//mTrips = trips;
        mTitle = (TextView) view.findViewById(R.id.trip_title);
        mTimeSpentLabel = (TextView) view.findViewById(R.id.time_display);
        mSpeedLabel = (TextView) view.findViewById(R.id.speed_display);
        mPointsLabel = (TextView) view.findViewById(R.id.points_display);
        mDistanceLabel = (TextView) view.findViewById(R.id.distance_display);
        mOptions= (ImageButton) view.findViewById(R.id.options);
        mOptions.setOnClickListener(this);
    }

    public void populate(TripSummary trip){
        mTrip = trip;
        mTitle.setText(""+trip.getId());
        mPointsLabel.setText(Integer.toString(trip.getLocationCount()));
    }

    @Override
    public void onClick(View v) {
        Trip trip = Trip.find(mTrip.getTripId(), this.getContext());
        openPopup(trip);
    }

    public void openPopup(final Trip trip){
        final long tripId = trip.getId();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.trip_pop_up, null, false);
        final PopupWindow pw = new PopupWindow(
                view,
                400,
                500,
                true);
        view.findViewById(R.id.sync_trip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
                uploadTrip(tripId);
            }
        });
        view.findViewById(R.id.view_trip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
                viewMap(tripId);
            }
        });

        view.findViewById(R.id.delete_trip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
                trip.delete();
                mTrips.remove(mTrip);
            }
        });
        pw.showAtLocation(mParent.findViewById(R.id.content_frame), Gravity.CENTER, 0, 0);
    }

    private void uploadTrip(long tripId){
        Trip trip =  Trip.find(tripId, getContext());
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.show();
        UploadTrip uploader = new UploadTrip(CloudentBuilder.getDatabase(getContext()), pd);
        uploader.execute(trip);
    }

    private void viewMap(long tripId){
        Intent intent = new Intent(getContext(), MapActivity.class);
        intent.putExtra("trip", tripId);
        mParent.startActivity(intent);
    }

    private Context getContext(){
        return mParent.getBaseContext();
    }
}
