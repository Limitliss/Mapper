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

public class LocationDbLocal implements LocationDb {
    private Context context;
    private LocationTableHelper mDbHelper;

    public LocationDbLocal(Context context){
        this.context = context;
        mDbHelper = new LocationTableHelper(context);
    }

    @Override
    public long addLocation(double longitude, double latitude, float accuracy){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocationTableContract.COLUMN_NAME_LATITUDE, latitude);
        values.put(LocationTableContract.COLUMN_NAME_LONGITUDE, longitude);
        values.put(LocationTableContract.COLUMN_NAME_TIMESTAMP, System.currentTimeMillis());
        values.put(LocationTableContract.COLUMN_NAME_ACCURACY, accuracy);
        long id = db.insert(LocationTableContract.TABLE_NAME, null, values);
        return id;
    }

    public Cursor getLocations(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] columns = {LocationTableContract.COLUMN_NAME_LATITUDE,
                LocationTableContract.COLUMN_NAME_LONGITUDE,
                LocationTableContract.COLUMN_NAME_ACCURACY};
        Cursor cursor = db.query(LocationTableContract.TABLE_NAME, columns, LocationTableContract.COLUMN_NAME_ACCURACY+ " < 20", null, null, null, LocationTableContract.COLUMN_NAME_TIMESTAMP + " ASC", null);
        return cursor;
    }

    @Override
    public int countRows(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String query = "select count(" + LocationTableContract._ID + ") as count from " + LocationTableContract.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            return cursor.getInt(cursor.getColumnIndex("count"));
        }
        return -1;
    }

    @Override
    public void clearDatabase(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        mDbHelper.onUpgrade(db,4,4);
    }
}
