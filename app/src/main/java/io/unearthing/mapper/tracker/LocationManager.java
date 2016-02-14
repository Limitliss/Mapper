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

import android.location.Location;
import com.google.android.gms.location.LocationListener;

public abstract class LocationManager implements  LocationListener {
    protected int mInterval = 1000;
    protected boolean mConnected = false;

    private GpsStatus mGpsStatus;

    private LocationManagerListener mLmListener = null;
    private LocationListener mListener = null;

    public LocationManager(LocationManagerListener listener) {
        mGpsStatus = new GpsStatus();
        mLmListener = listener;
    }

    protected void onConnected(){
        mLmListener.onConnected();
    }

    public void stop(){
        this.mConnected = false;
        this.mLmListener.onDisconnected();
    }

    public void start(LocationListener listener){
        this.mListener = listener;
    }

    @Override
    public void onLocationChanged(Location location) {
        boolean wasFixed = isGpsFixed();
        boolean isFixed = mGpsStatus.onLocationChanged(location);

        if(wasFixed != isFixed){
            if(isFixed){
                mLmListener.onGPSFixed();
            } else {
                mLmListener.onLostGPSFix();
            }
        }

        if(!isFixed){
            mLmListener.onGPSFixing(mGpsStatus.timeTillFix());
        }

        if(isFixed){
            mListener.onLocationChanged(location);
        }
    }

    public int getmInterval() {
        return mInterval;
    }

    public void setmInterval(int mInterval) {
        this.mInterval = mInterval;
    }

    public boolean isGpsFixed(){
        return mGpsStatus.IsFixed();
    }

    public boolean ismConnected(){
        return mConnected;
    }
}
