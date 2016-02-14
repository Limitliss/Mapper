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

import android.annotation.TargetApi;
import android.location.Location;
import android.os.Build;

/**
 *
 * This is a helper class that is used to determine when the GPS status is good
 * enough (isFixed())
 *
 */

@TargetApi(Build.VERSION_CODES.FROYO)
public class GpsStatus {

    boolean mIsFixed = false;

    /**
     * If we get a location with accurancy <= mFixAccurancy mFixed => true
     */
    final float mFixAccurancy = 20;

    /**
     * If we get fixed satellites >= mFixSatellites mFixed => true
     */
    final int mFixSatellites = 2;

    /**
     * Minum time to fix GPS
     * true
     */
    final long mFixTime = 30000;

    private float averageAccuracy = -1;
    private float minAccuracy = -1;
    private long startFixing = -1;

    int mKnownSatellites = 0;
    int mUsedInLastFixSatellites = 0;

    public GpsStatus(){
    }

    public boolean onLocationChanged(Location location) {
        if(startFixing == -1){
            startFixing = location.getTime();
        }
        
        if(location.hasAccuracy()){
            float accuracy = location.getAccuracy();
            if(minAccuracy > 0){
                minAccuracy = accuracy;
            } else if (accuracy > minAccuracy) {
                minAccuracy = accuracy;
                averageAccuracy = accuracy;
            }

            averageAccuracy = (averageAccuracy + accuracy) / 2;

            if( accuracy < mFixAccurancy){
                mIsFixed = true;
                return mIsFixed;
            }

        }

        if (mKnownSatellites >= mFixSatellites) {
            mIsFixed = true;
        } else if ((location.getTime() - startFixing) > mFixTime){
            mIsFixed = true;
        }
        return mIsFixed;
    }

    public long timeTillFix(){
        return System.currentTimeMillis() - startFixing;
    }

    private void clear(boolean resetIsFixed) {
        if (resetIsFixed) {
            mIsFixed = false;
        }
        mKnownSatellites = 0;
    }

    public int getSatellitesAvailable() {
        return mKnownSatellites;
    }

    public int getSatellitesFixed() {
        return mUsedInLastFixSatellites;
    }

    public boolean IsFixed() {
        return mIsFixed;
    }

}
