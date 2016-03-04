package io.unearthing.mapper.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by billybonks on 16/2/16.
 */
public  class TableOpener extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 8;
    public static final String DATABASE_NAME = "LocationStore.db";

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

    public static abstract class TripTableContract implements BaseColumns {
        public static final String TABLE_NAME = "trip";
        public static final String COLUMN_NAME_START = "start";
        public static final String COLUMN_NAME_END = "end";
        public static final String COLUMN_NAME_TITLE = "title";
    }

    private static final String SQL_CREATE_LOCATION_TABLE =
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

    private static final String SQL_DELETE_LOCATION_TABLE =
            "DROP TABLE IF EXISTS " + LocationTableContract.TABLE_NAME;

    private static final String SQL_CREATE_TRIP_TABLE =
            "CREATE TABLE " + TripTableContract.TABLE_NAME + "(" +
                    TripTableContract._ID                   + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TripTableContract.COLUMN_NAME_START  +" INTEGER," +
                    TripTableContract.COLUMN_NAME_END +" INTEGER," +
                    TripTableContract.COLUMN_NAME_TITLE +" STRING" +
                    ")";
    private static final String SQL_DELETE_TRIP_TABLE =
            "DROP TABLE IF EXISTS " + TripTableContract.TABLE_NAME;

    public TableOpener(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_LOCATION_TABLE);
        db.execSQL(SQL_CREATE_TRIP_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_LOCATION_TABLE);
        db.execSQL(SQL_DELETE_TRIP_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
