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

package io.unearthing.mapper.ui.fragments;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.app.Fragment;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.unearthing.mapper.ILocationService;
import io.unearthing.mapper.services.LocationService;
import io.unearthing.mapper.R;

public class ControlFragment extends Fragment implements View.OnClickListener {

    private ILocationService mLocationService;
    private Button mControlButton;
    private Activity mActivity;

    private final String mServiceName = "io.unearthing.mapper.services.LocationService";
    private boolean mServiceRunning = false;

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

    public ControlFragment() {

    }

    /*
        LIFE CYCLE OVERRIDES
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mActivity = getActivity();
        //TODO: try not to use getActivity
        // Context context = (Context)getContext();
        View view = inflater.inflate(R.layout.fragment_control, container, false);
        mControlButton = (Button) view.findViewById(R.id.service_control_button);
        boolean serviceStarted = isServiceStarted();
        if(serviceStarted){
            bindService();
        }
        setServiceRunning(serviceStarted);
        mControlButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(isServiceRunning()){
            stopService();
        }else{
            startService();
        }
    }

    private void startService(){
        Intent intent = new Intent(mActivity, LocationService.class);
        mActivity.startService(intent);
        this.bindService();
        setServiceRunning(true);
    }

    private void bindService(){
        Intent intent = new Intent(mActivity, LocationService.class);
        mActivity.bindService(intent, mConnection, mActivity.BIND_AUTO_CREATE);
    }

    private void stopService(){
        Intent intent = new Intent(mActivity, LocationService.class);
        mActivity.stopService(intent);
        mActivity.unbindService(mConnection);
        setServiceRunning(false);
    }


    private boolean isServiceStarted(){
        Activity activity = getActivity();
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service: manager.getRunningServices(Integer.MAX_VALUE)){
            if(mServiceName.equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    public boolean isServiceRunning() {
        return mServiceRunning;
    }

    public void setServiceRunning(boolean serviceRunning) {
        if(serviceRunning){
            mControlButton.setText(R.string.stop_tracking);
        }else{
            mControlButton.setText(R.string.start_tracking);
        }
        this.mServiceRunning = serviceRunning;
    }
}
