package io.unearthing.mapper.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//import com.google.gson.Gson;

import com.google.gson.Gson;

import io.unearthing.mapper.model.definitions.LocationTableHelper;
import io.unearthing.mapper.model.definitions.TripTableHelper;

/**
 * Created by billybonks on 16/2/16.
 */
public class Trip {
    private long mId = -1;
    private String mTitle;
    private long mStartTime;
    private long mEndTime;
    private TripTableHelper mTripTable;
    private LocationTableHelper mLocationTable;
    public Trip(Context context){
        mTripTable = new TripTableHelper(context);
        mLocationTable = new LocationTableHelper(context);
    }

    public Cursor getLocations() {
        if(mId > -1) {
            SQLiteDatabase db = mLocationTable.getReadableDatabase();
            String[] columns = {LocationTableHelper.LocationTableContract.COLUMN_NAME_LATITUDE,
                    LocationTableHelper.LocationTableContract.COLUMN_NAME_LONGITUDE,
                    LocationTableHelper.LocationTableContract.COLUMN_NAME_ACCURACY};
            return db.query(LocationTableHelper.LocationTableContract.TABLE_NAME, columns,
                    LocationTableHelper.LocationTableContract.COLUMN_NAME_TRIP + " = "+ mId,
                    null, null, null, LocationTableHelper.LocationTableContract.COLUMN_NAME_TIMESTAMP + " ASC", null);
        } else {
            throw new Error("Needs a trip id");
        }
    }

    public long start(){
        SQLiteDatabase db = mTripTable.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TripTableHelper.TripTableContract.COLUMN_NAME_START, System.currentTimeMillis());
        values.put(TripTableHelper.TripTableContract.COLUMN_NAME_TITLE, "Untitled");
        mId = db.insert(TripTableHelper.TripTableContract.TABLE_NAME, null, values);
        return mId;
    }

    public boolean end(){
        SQLiteDatabase db = mTripTable.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TripTableHelper.TripTableContract.COLUMN_NAME_END, System.currentTimeMillis());
        int rowsAffected = db.update(TripTableHelper.TripTableContract.TABLE_NAME, values,"_ID = ?",new String[]{Long.toString(mId)});
        if (rowsAffected == 1) return true;
        return false;
    }

    public boolean create(){
        SQLiteDatabase db = mTripTable.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TripTableHelper.TripTableContract.COLUMN_NAME_START, this.mStartTime);
        values.put(TripTableHelper.TripTableContract.COLUMN_NAME_END, this.mEndTime);
        values.put(TripTableHelper.TripTableContract.COLUMN_NAME_TITLE, this.mTitle);
        mId = db.insert(TripTableHelper.TripTableContract.TABLE_NAME, null, values);
        return mId > 0;
    }

    public boolean delete(){
        return false;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setmTitle(String title) {
        this.mTitle = title;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(long startTime) {
        this.mStartTime = startTime;
    }

    public long getEndTime() {
        return mEndTime;
    }

    public void setEndTime(long endTime) {
        this.mEndTime = endTime;
    }

    public String toString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
