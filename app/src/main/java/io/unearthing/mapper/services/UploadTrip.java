package io.unearthing.mapper.services;

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import com.cloudant.client.api.Database;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import io.unearthing.mapper.model.Location;
import io.unearthing.mapper.model.Trip;

/**
 * Created by billybonks on 20/2/16.
 */
public class UploadTrip extends AsyncTask<Trip,Integer,Integer> {
    Database mDb;

    public UploadTrip(Database db){
        mDb = db;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Trip... params) {
        for(Trip trip : params) {
            JsonElement jelement = new JsonParser().parse(trip.toString());
            JsonObject jobject = jelement.getAsJsonObject();
            mDb.save(jobject);
            List<Location> locations = trip.getLocations();
            Gson gson = new GsonBuilder().create();
            Location loc = locations.get(0);
            String json = gson.toJson(loc);
            mDb.bulk(locations);
        }
        return 3;
    }
}
