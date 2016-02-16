/*
 * Copyright (C) 2016 sebastienstettler@gmail.com
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.unearthing.mapper.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import io.unearthing.mapper.model.definitions.LocationTableHelper;
import io.unearthing.mapper.model.definitions.LocationTableHelper.LocationTableContract;
import io.unearthing.mapper.model.definitions.TripTableHelper;
import io.unearthing.mapper.model.definitions.TripTableHelper.TripTableContract;

public class LocationDbLocal implements LocationDb {
    private LocationTableHelper mLocationTable;
    private TripTableHelper mTripTable;

    public LocationDbLocal(Context context){
        mLocationTable = new LocationTableHelper(context);
        mTripTable = new TripTableHelper(context);
    }

    @Override
    public long addLocation(double longitude, double latitude, float accuracy,long tripID){
        SQLiteDatabase db = mLocationTable.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocationTableContract.COLUMN_NAME_LATITUDE, latitude);
        values.put(LocationTableContract.COLUMN_NAME_LONGITUDE, longitude);
        values.put(LocationTableContract.COLUMN_NAME_TIMESTAMP, System.currentTimeMillis());
        values.put(LocationTableContract.COLUMN_NAME_ACCURACY, accuracy);
        values.put(LocationTableContract.COLUMN_NAME_TRIP, tripID);
        long id = db.insert(LocationTableContract.TABLE_NAME, null, values);
        return id;
    }

    public Cursor getLocations(){
        SQLiteDatabase db = mLocationTable.getReadableDatabase();
        String[] columns = {LocationTableContract.COLUMN_NAME_LATITUDE,
                LocationTableContract.COLUMN_NAME_LONGITUDE,
                LocationTableContract.COLUMN_NAME_ACCURACY};
        Cursor cursor = db.query(LocationTableContract.TABLE_NAME, columns, LocationTableContract.COLUMN_NAME_ACCURACY + " < 20", null, null, null, LocationTableContract.COLUMN_NAME_TIMESTAMP + " ASC", null);
        return cursor;
    }

    @Override
    public int countRows(){
        SQLiteDatabase db = mLocationTable.getReadableDatabase();
        String query = "select count(" + LocationTableContract._ID + ") as count from " + LocationTableContract.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            return cursor.getInt(cursor.getColumnIndex("count"));
        }
        return -1;
    }

    public Cursor getTrips(){
        SQLiteDatabase db = mTripTable.getReadableDatabase();
        //String query = "SELECT t.title as title, count(l._id) as count from trip t INNER JOIN location l on t._id = l.trip WHERE t._id = 2";
        String query = "SELECT _id, title from trip";
        return db.rawQuery(query,null);
    }

    @Override
    public void clearDatabase(){
        SQLiteDatabase db = mLocationTable.getWritableDatabase();
        mLocationTable.onUpgrade(db, 4, 4);
    }

    @Override
    public long startSession() {
        SQLiteDatabase db = mTripTable.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TripTableContract.COLUMN_NAME_START, System.currentTimeMillis());
        values.put(TripTableContract.COLUMN_NAME_TITLE, "Untitled");
        return db.insert(TripTableContract.TABLE_NAME, null, values);
    }

    @Override
    public boolean endSession(long id) {
        SQLiteDatabase db = mTripTable.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TripTableContract.COLUMN_NAME_END, System.currentTimeMillis());
        int rowsAffected = db.update(TripTableContract.TABLE_NAME, values,"_ID = ?",new String[]{Long.toString(id)});
        if (rowsAffected == 1) return true;
        return false;
    }
}
