package io.unearthing.mapper.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import io.unearthing.mapper.model.definitions.TableOpener;

/**
 * Created by billybonks on 16/2/16.
 */
public class Location extends AbstractLocation {

    private TableOpener mTableOpener;

    public Location(Cursor cursor) {
        mLatitude =  cursor.getDouble(cursor.getColumnIndex(TableOpener.LocationTableContract.COLUMN_NAME_LATITUDE));
        mLongitude = cursor.getDouble(cursor.getColumnIndex(TableOpener.LocationTableContract.COLUMN_NAME_LONGITUDE));
        mAltitude = cursor.getDouble(cursor.getColumnIndex(TableOpener.LocationTableContract.COLUMN_NAME_ALTITUDE));
        mSpeed = cursor.getDouble(cursor.getColumnIndex(TableOpener.LocationTableContract.COLUMN_NAME_SPEED));
        mAccuracy = cursor.getFloat(cursor.getColumnIndex(TableOpener.LocationTableContract.COLUMN_NAME_ACCURACY));
        mBearing = cursor.getFloat(cursor.getColumnIndex(TableOpener.LocationTableContract.COLUMN_NAME_BEARING));
        mTimeStamp = cursor.getFloat(cursor.getColumnIndex(TableOpener.LocationTableContract.COLUMN_NAME_TIMESTAMP));
        mTripId = cursor.getLong(cursor.getColumnIndex(TableOpener.LocationTableContract.COLUMN_NAME_TRIP));
    }

    public Location(Context context){
        mTableOpener = new TableOpener(context);
    }

    public boolean save(){
        SQLiteDatabase db = mTableOpener.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TableOpener.LocationTableContract.COLUMN_NAME_LATITUDE, this.mLatitude);
        values.put(TableOpener.LocationTableContract.COLUMN_NAME_LONGITUDE, this.mLongitude);
        values.put(TableOpener.LocationTableContract.COLUMN_NAME_TIMESTAMP, this.mTimeStamp);
        values.put(TableOpener.LocationTableContract.COLUMN_NAME_ACCURACY, this.mAccuracy);
        values.put(TableOpener.LocationTableContract.COLUMN_NAME_BEARING, this.mBearing);
        values.put(TableOpener.LocationTableContract.COLUMN_NAME_SPEED, this.mSpeed);
        values.put(TableOpener.LocationTableContract.COLUMN_NAME_ALTITUDE, this.mAltitude);
        values.put(TableOpener.LocationTableContract.COLUMN_NAME_TRIP, this.mTripId);
        mId = db.insert(TableOpener.LocationTableContract.TABLE_NAME, null, values);
        return mId > 0;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public float getAccuracy() {
        return mAccuracy;
    }

    public void setAccuracy(float accuracy) {
        this.mAccuracy = accuracy;
    }

    public float getBearing() {
        return mBearing;
    }

    public void setBearing(float bearing) {
        this.mBearing = bearing;
    }

    public double getAltitude() {
        return mAltitude;
    }

    public void setAltitude(double altitude) {
        this.mAltitude = altitude;
    }

    public double getSpeed() {
        return mSpeed;
    }

    public void setSpeed(double speed) {
        this.mSpeed = speed;
    }

    public float getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(float timeStamp) {
        this.mTimeStamp = timeStamp;
    }

    public long getTripId() {
        return mTripId;
    }

    public void setTripId(long tripId) {
        this.mTripId = tripId;
    }

}
