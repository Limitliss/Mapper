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

package io.unearthing.mapper.model.definitions;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class LocationTableHelper extends SQLiteOpenHelper {
    public static abstract class LocationTableContract implements BaseColumns {

        public static final String TABLE_NAME = "location";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_ACCURACY = "accuracy";
        public static final String COLUMN_NAME_SPEED = "speed";
        public static final String COLUMN_NAME_PROVIDER = "provider";
        public static final String COLUMN_NAME_BEARING = "bearing";
        public static final String COLUMN_NAME_ALTITUDE = "altitude";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_TRIP  = "trip";

    }
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + LocationTableContract.TABLE_NAME  + "(" +
                    LocationTableContract._ID                   + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    LocationTableContract.COLUMN_NAME_TRIP      +" INTEGER," +
                    LocationTableContract.COLUMN_NAME_PROVIDER  +" TEXT," +
                    LocationTableContract.COLUMN_NAME_LATITUDE  +" REAL," +
                    LocationTableContract.COLUMN_NAME_LONGITUDE +" REAL," +
                    LocationTableContract.COLUMN_NAME_ACCURACY  +" REAL," +
                    LocationTableContract.COLUMN_NAME_SPEED     +" REAL," +
                    LocationTableContract.COLUMN_NAME_BEARING   +" REAL," +
                    LocationTableContract.COLUMN_NAME_ALTITUDE  +" REAL," +
                    LocationTableContract.COLUMN_NAME_TIMESTAMP +" INTEGER" +
                    ")";
    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + LocationTableContract.TABLE_NAME;

    public static final int DATABASE_VERSION = 8;
    public static final String DATABASE_NAME = "LocationStore.db";

    public LocationTableHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}