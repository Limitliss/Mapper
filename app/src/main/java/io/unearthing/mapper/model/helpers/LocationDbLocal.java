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
        SQLiteDatabase tdb = mTripTable.getWritableDatabase();
        mTripTable.onUpgrade(tdb,4,4);
    }
}
