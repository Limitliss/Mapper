package io.unearthing.mapper.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by billybonks on 4/3/16.
 */
public class TripSummary {

    private TableOpener mTableOpener;
    private Trip mTrip;
    private Location lastLocation;

    private long mId = -1;
    private long mTripId = -1;
    private float mDistance;
    private double mSpeed;
    private int mPoints;
    private long mDuration;

    public TripSummary(Context context){
        mTableOpener = new TableOpener(context);
    }

    public TripSummary(Context context, Trip trip){
        this(context);
        this.setTrip(trip);
    }

    public TripSummary(Context context, Cursor cursor) {
        this(context);
        mId = cursor.getLong(cursor.getColumnIndex(TableOpener.TripSummaryTableContract._ID));
        mDistance = cursor.getLong(cursor.getColumnIndex(TableOpener.TripSummaryTableContract.COLUMN_NAME_DISTANCE));
        mSpeed = cursor.getLong(cursor.getColumnIndex(TableOpener.TripSummaryTableContract.COLUMN_NAME_SPEED));
        mPoints = cursor.getInt(cursor.getColumnIndex(TableOpener.TripSummaryTableContract.COLUMN_NAME_LOCATIONS));
        mDuration = cursor.getLong(cursor.getColumnIndex(TableOpener.TripSummaryTableContract.COLUMN_NAME_DURATION));
        mTripId =  cursor.getLong(cursor.getColumnIndex(TableOpener.TripSummaryTableContract.COLUMN_NAME_TRIP));
    }

    public float getDistance(){
        return mDistance;
    }

    public long getId(){
        return mId;
    }

    public long getTripId(){
        return mTripId;
    }

    public long getDuration(){
        if(mTrip  != null){
            long endTime = mTrip.getEndTime();
            if(endTime == -1){
                endTime =  System.currentTimeMillis();
            }
            return endTime - mTrip.getStartTime();
        }
        return mDuration;
    }

    public int getLocationCount(){
        return mPoints;
    }

    public float getSpeed(){
        return mPoints;
    }

    public void setTrip(Trip trip){
        mTrip = trip;
        mTripId = trip.getId();
    }

    public void addLocation(Location location){
        mPoints++;
        mDuration = getDuration();
        double speedSum = mSpeed +location.getSpeed();
        mSpeed = speedSum/2;
        if(lastLocation != null){
            mDistance += lastLocation.getRawLocation().distanceTo(location.getRawLocation());
        }
        lastLocation = location;
        mTrip.addLocation(location);
    }

    public static TripSummary find(long id, Context context){
        String table = TableOpener.TripSummaryTableContract.TABLE_NAME;
        String where = "_ID = ?";
        String[] args = new String[]{Long.toString(id)};
        SQLiteDatabase db = new TableOpener(context).getReadableDatabase();
        Cursor c = db.query(table, null, where, args, null, null, null);
        TripSummary trip = new TripSummary(context,c);
        c.close();
        return trip;

    }

    public boolean save(){
        if(mId > -1) {
            SQLiteDatabase db = mTableOpener.getWritableDatabase();
            String where = TableOpener.TripSummaryTableContract._ID + "  = ?";
            int rowCount = db.update(TableOpener.TripSummaryTableContract.TABLE_NAME,getValues(),where,new String[]{""+mId});
            if(rowCount ==  1){
                return true;
            }
            return false;
        }
        return create();
    }


    public boolean create(){
        SQLiteDatabase db = mTableOpener.getWritableDatabase();
        mId = db.insert(TableOpener.TripSummaryTableContract.TABLE_NAME, null, getValues());
        return mId > 0;
    }

    public ContentValues getValues(){
        mDuration = getDuration();
        ContentValues values = new ContentValues();
        values.put(TableOpener.TripSummaryTableContract.COLUMN_NAME_SPEED, this.getSpeed());
        values.put(TableOpener.TripSummaryTableContract.COLUMN_NAME_LOCATIONS, this.getLocationCount());
        values.put(TableOpener.TripSummaryTableContract.COLUMN_NAME_DURATION, this.getDuration());
        values.put(TableOpener.TripSummaryTableContract.COLUMN_NAME_DISTANCE, this.getDistance());
        values.put(TableOpener.TripSummaryTableContract.COLUMN_NAME_TRIP, this.mTrip.getId());
        return values;
    }
    public static List<TripSummary> findAll(Context context){
        SQLiteDatabase db = new TableOpener(context).getReadableDatabase();
        String query = "SELECT * from trip_summary order by _id DESC";
        Cursor c =  db.rawQuery(query, null);
        List<TripSummary> trips = new ArrayList<>();
        while(c.moveToNext()) {
            trips.add(new TripSummary(context,c));
        }
        c.close();
        return trips;
    }
}
