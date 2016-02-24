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

package io.unearthing.mapper.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;


import com.facebook.stetho.Stetho;

import junit.framework.Test;

import io.unearthing.mapper.ILocationService;
import io.unearthing.mapper.R;
import io.unearthing.mapper.model.Trip;
import io.unearthing.mapper.services.LocationService;
import io.unearthing.mapper.services.UploadTrip;

public class MainActivity extends AbstractActivity {

    private final static int REQUEST_FINE_LOCATION_PERMISSION = 3;

    private final String mServiceName = "io.unearthing.mapper.services.LocationService";
    private boolean mServiceRunning = false;
    private ILocationService mLocationService;

    private ServiceConnection mConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLocationService = ILocationService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLocationService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectItem(0);
                }
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        boolean serviceStarted = isServiceStarted();

        if(serviceStarted){
            bindService();
        }
        setServiceRunning(serviceStarted);
        int res = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(res == PackageManager.PERMISSION_GRANTED) {
            selectItem(0);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_FINE_LOCATION_PERMISSION);

        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(isServiceRunning()){
            this.unbindService(mConnection);
        }
    }

    public boolean isServiceRunning() {
        return mServiceRunning;
    }

    public void setServiceRunning(boolean serviceRunning) {
        FloatingActionButton button = (FloatingActionButton)this.findViewById(R.id.service_control_button);
        Drawable gfx;
        if(serviceRunning){
            gfx = getDrawable(R.drawable.ic_media_pause);
        }else{
            gfx = getDrawable(R.drawable.ic_media_play);
        }
        button.setImageDrawable(gfx);
        this.mServiceRunning = serviceRunning;
    }

    public void onServiceControllerClicked(View view){
        if(isServiceRunning()){
            stopService();
        }else{
            startService();
        }
    }

    private void startService(){
        Intent intent = new Intent(this, LocationService.class);
        this.startService(intent);
        this.bindService();
        setServiceRunning(true);
    }

    private void bindService(){
        Intent intent = new Intent(this, LocationService.class);
        this.bindService(intent, mConnection, this.BIND_AUTO_CREATE);
    }

    private void stopService(){
        Intent intent = new Intent(this, LocationService.class);
        this.stopService(intent);
        this.unbindService(mConnection);
        setServiceRunning(false);
    }


    private boolean isServiceStarted(){
        ActivityManager manager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service: manager.getRunningServices(Integer.MAX_VALUE)){
            if(mServiceName.equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }
}

