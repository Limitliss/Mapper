package io.unearthing.mapper.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

//import com.google.gson.Gson;

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

    public Trip(Context context){
        mTripTable = new TripTableHelper(context);
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

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(long mStartTime) {
        this.mStartTime = mStartTime;
    }

    public long getEndTime() {
        return mEndTime;
    }

    public void setEndTime(long mEndTime) {
        this.mEndTime = mEndTime;
    }

//    public String toString(){
//        Gson gson = new Gson();
//        return gson.toJson(this);
//    }
}
