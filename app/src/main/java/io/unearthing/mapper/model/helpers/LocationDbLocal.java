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

package io.unearthing.mapper.model.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import io.unearthing.mapper.model.Location;
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


    public Cursor getLocations(){
        SQLiteDatabase db = mLocationTable.getReadableDatabase();
        String[] columns = {LocationTableContract.COLUMN_NAME_LATITUDE,
                LocationTableContract.COLUMN_NAME_LONGITUDE,
                LocationTableContract.COLUMN_NAME_ACCURACY};
        Cursor cursor = db.query(LocationTableContract.TABLE_NAME, columns, LocationTableContract.COLUMN_NAME_ACCURACY + " < 20", null, null, null, LocationTableContract.COLUMN_NAME_TIMESTAMP + " ASC", null);
        return cursor;
    }

    public Cursor getTrips(){
        SQLiteDatabase db = mTripTable.getReadableDatabase();
        //String query = "SELECT t.title as title, count(l._id) as count from trip t INNER JOIN location l on t._id = l.trip WHERE t._id = 2";
        String query = "SELECT _id, title from trip";
        return db.rawQuery(query,null);
    }

    @Override
    public int countTripLocations() {
        SQLiteDatabase db = mLocationTable.getReadableDatabase();
        String query = "select count(" + TripTableContract._ID + ") as count from " + LocationTableContract.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            return cursor.getInt(cursor.getColumnIndex("count"));
        }
        return -1;
    }

    @Override
    public int countTrips() {
        SQLiteDatabase db = mTripTable.getReadableDatabase();
        String query = "select count(" + TripTableContract._ID + ") as count from " + TripTableContract.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            return cursor.getInt(cursor.getColumnIndex("count"));
        }
        return -1;
    }

    @Override
    public void clearDatabase(){
        SQLiteDatabase db = mLocationTable.getWritableDatabase();
        mLocationTable.onUpgrade(db, 4, 4);
        SQLiteDatabase tripDB = mTripTable.getWritableDatabase();
        mLocationTable.onUpgrade(tripDB, 4, 4);
    }

    @Override
    public long addSession(long startTime, long endTime, String title) {
        SQLiteDatabase db = mTripTable.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TripTableContract.COLUMN_NAME_START, startTime);
        values.put(TripTableContract.COLUMN_NAME_END, endTime);
        values.put(TripTableContract.COLUMN_NAME_TITLE, title);
        return db.insert(TripTableContract.TABLE_NAME, null, values);
    }

    @Override
    public int deleteSession(long id) {
        return 0;
    }

    @Override
    public int deleteSessionLocations(long sessionId) {
        return 0;
    }

    @Override
    public int[] addBulkLocations(Location[] locations) {
        return new int[0];
    }
}
