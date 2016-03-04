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

package io.unearthing.mapper.tracker;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;
import android.os.Vibrator;

import com.google.android.gms.location.LocationListener;

import io.unearthing.mapper.model.TableOpener;
import io.unearthing.mapper.model.Trip;

public class Tracker implements LocationListener, LocationManagerListener {

    private Context context;
    private Trip mTrip;

    public Tracker(Context context){
        this.context = context;
    }

    @Override
    public void onLocationChanged(Location location) {
        float accuracy = -1;
        if(location.hasAccuracy()){
            accuracy = location.getAccuracy();
        }
        io.unearthing.mapper.model.Location mLocation = new io.unearthing.mapper.model.Location(context);
        mLocation.setAccuracy(accuracy);
        mLocation.setAltitude(location.getAltitude());
        mLocation.setBearing(location.getBearing());
        mLocation.setLatitude(location.getLatitude());
        mLocation.setLongitude(location.getLongitude());
        mLocation.setSpeed(location.getSpeed());
        mLocation.setTimeStamp(location.getTime());
        mLocation.save();
        mTrip.addLocation(mLocation);
        Toast.makeText(context, Float.toString(accuracy), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected() {
        mTrip = new Trip(context);
        mTrip.start();
        Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGPSFixing(float timeTillFix) {
        Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(20);
        Toast.makeText(context,""+timeTillFix, Toast.LENGTH_SHORT).show();
        v.vibrate(20);
    }

    @Override
    public void onGPSFixed() {
        Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(1000);
    }

    @Override
    public void onLostGPSFix() {
        Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(20);
    }
    @Override
    public void onDisconnected() {
        mTrip.end();
        Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show();
    }
}
