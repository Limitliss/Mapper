package io.unearthing.mapper.model.definitions;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by billybonks on 16/2/16.
 */
public abstract class TableOpener extends SQLiteOpenHelper {
    public TableOpener(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static final int DATABASE_VERSION = 8;
    public static final String DATABASE_NAME = "LocationStore.db";
}
