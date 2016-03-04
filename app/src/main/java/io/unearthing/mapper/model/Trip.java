package io.unearthing.mapper.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by billybonks on 16/2/16.
 */
public class Trip  extends AbstractTrip {

    private List<Location> mLocations;
    private TableOpener mTableOpener;
    private CloudantClient client;
    private Database db;


    public Trip(Context context){
        mTableOpener = new TableOpener(context);
        mLocations = new ArrayList<Location>();
    }

    public Trip(Context context, Cursor cursor) {
        this(context);
        mId = cursor.getLong(cursor.getColumnIndex(TableOpener.TripTableContract._ID));
        mStartTime = cursor.getLong(cursor.getColumnIndex(TableOpener.TripTableContract.COLUMN_NAME_START));
        mEndTime = cursor.getLong(cursor.getColumnIndex(TableOpener.TripTableContract.COLUMN_NAME_END));
        mTitle = cursor.getString(cursor.getColumnIndex(TableOpener.TripTableContract.COLUMN_NAME_TITLE));
    }

    public long start(){
        SQLiteDatabase db = mTableOpener.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableOpener.TripTableContract.COLUMN_NAME_START, System.currentTimeMillis());
        values.put(TableOpener.TripTableContract.COLUMN_NAME_TITLE, "Untitled");
        mId = db.insert(TableOpener.TripTableContract.TABLE_NAME, null, values);
        return mId;
    }

    public boolean end(){
        SQLiteDatabase db = mTableOpener.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableOpener.TripTableContract.COLUMN_NAME_END, System.currentTimeMillis());
        int rowsAffected = db.update(TableOpener.TripTableContract.TABLE_NAME, values,"_ID = ?",new String[]{Long.toString(mId)});
        if (rowsAffected == 1) return true;
        return false;
    }

    public void addLocation(Location location) {
        location.setTripId(this.mId);
        mLocations.add(location);
    }

    public List<Location> getLocations() {
        if(mLocations.size() > 0 && mId > -1){
            Cursor c = this.getLocationsCursor();
            mLocations = new ArrayList<Location>();
            int counter = 0;
            while(c.moveToNext()){
                mLocations.add( new Location(c));
                counter++;
            }
        }
        return mLocations;
    }


    public static Trip find(long id, Context context){
        String table = TableOpener.TripTableContract.TABLE_NAME;
        String where = "_ID = ?";
        String[] args = new String[]{Long.toString(id)};
        SQLiteDatabase db = new TableOpener(context).getReadableDatabase();
        Cursor c = db.query(table, null, where, args, null, null, null);
        Trip trip = new Trip(context,c);
        c.close();
        return trip;

    }

    public static List<Trip> findAll(Context context){
        SQLiteDatabase db = new TableOpener(context).getReadableDatabase();
        String query = "SELECT * from trip order by _id DESC";
        Cursor c =  db.rawQuery(query, null);
        List<Trip> trips = new ArrayList<>();
        while(c.moveToNext()) {
            trips.add(new Trip(context,c));
        }
        c.close();
        return trips;
    }

    private Cursor getLocationsCursor() {
        if(mId > -1) {
            SQLiteDatabase db = mTableOpener.getReadableDatabase();
            String[] columns = {TableOpener.LocationTableContract.COLUMN_NAME_LATITUDE,
                    TableOpener.LocationTableContract.COLUMN_NAME_LONGITUDE,
                    TableOpener.LocationTableContract.COLUMN_NAME_ACCURACY};
            return db.query(TableOpener.LocationTableContract.TABLE_NAME, null,
                    TableOpener.LocationTableContract.COLUMN_NAME_TRIP + " = "+ mId,
                    null, null, null, TableOpener.LocationTableContract.COLUMN_NAME_TIMESTAMP + " ASC", null);
        } else {
            throw new Error("Needs a trip id");
        }
    }


    public boolean delete(){
        SQLiteDatabase tDb = mTableOpener.getWritableDatabase();
        String [] args =  new String[]{Long.toString(this.mId)};
        int rowCount = tDb.delete(TableOpener.TripTableContract.TABLE_NAME,
                "_id = ?",
               args);
        int locationsDeletedCount = tDb.delete(TableOpener.LocationTableContract.TABLE_NAME,
                TableOpener.LocationTableContract.COLUMN_NAME_TRIP +" = ?",
                args);
        int locationCount = mLocations.size();
        if((rowCount == 1) && (locationsDeletedCount == locationCount)){
            return true;
        }else{
            return false;
        }
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

    public void setTitle(String title) {
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

    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this, AbstractTrip.class);
    }
}
