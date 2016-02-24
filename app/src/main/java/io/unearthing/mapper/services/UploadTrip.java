package io.unearthing.mapper.services;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import com.cloudant.client.api.Database;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.List;

import io.unearthing.mapper.model.Location;
import io.unearthing.mapper.model.Trip;
import io.unearthing.mapper.model.helpers.AbstractLocation;

/**
 * Created by billybonks on 20/2/16.
 */
public class UploadTrip extends AsyncTask<Trip,Integer,Integer> {
    Database mDb;
    ProgressDialog mPd;

    public UploadTrip(Database db, ProgressDialog pd){
        mDb = db;
        mPd = pd;
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
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
            publishProgress(calculateProgress(1));
            mDb.save(jobject);
            publishProgress(calculateProgress(2));
            List<JsonObject> locations = trip.getLocationsAsJsonObject();
            publishProgress(calculateProgress(3));
            mDb.bulk(locations);
            publishProgress(calculateProgress(4));
        }
        return 1;
    }
    private int calculateProgress(int step){
        return (step/4) * 10000;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        mPd.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(Integer result) {
        mPd.dismiss();
    }
}
