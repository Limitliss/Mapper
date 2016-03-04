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

import android.app.ActivityManager;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.unearthing.mapper.ILocationService;
import io.unearthing.mapper.R;
import io.unearthing.mapper.model.Trip;
import io.unearthing.mapper.services.LocationService;
import io.unearthing.mapper.ui.TripAdapter;

public class TripListFragment extends Fragment {

    List<Trip> mTrips;

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

    public TripListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup parentVw = container;
        View view = inflater.inflate(R.layout.fragment_trip_list, container, false);
        mTrips =new Trip(getContext()).findAll(getContext());
        for(int i = 0; i < mTrips.size(); i++) {
            mTrips.get(i).getLocations();
        }
        TripAdapter adapter = new TripAdapter(mTrips, getActivity());
        RecyclerView mRecyclerView = (RecyclerView)view.findViewById(R.id.trip_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        bindFab();
        boolean serviceStarted = isServiceStarted();

        if(serviceStarted){
            bindService();
        }
        setServiceRunning(serviceStarted);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(isServiceRunning()){
            getActivity().unbindService(mConnection);
        }
    }

    public void bindFab(){
        FloatingActionButton button = (FloatingActionButton)getActivity().findViewById(R.id.service_control_button);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(isServiceRunning()){
                    stopService();
                }else{
                    startService();
                }
            }
        });
    }
    public boolean isServiceRunning() {
        return mServiceRunning;
    }

    public void setServiceRunning(boolean serviceRunning) {
        FloatingActionButton button = (FloatingActionButton)getActivity().findViewById(R.id.service_control_button);
        Drawable gfx;
        if(serviceRunning){
            gfx = getActivity().getDrawable(R.drawable.ic_media_pause);
        }else{
            gfx = getActivity().getDrawable(R.drawable.ic_media_play);
        }
        button.setImageDrawable(gfx);
        this.mServiceRunning = serviceRunning;
    }


    private void startService(){
        Intent intent = new Intent(getActivity(), LocationService.class);
        getActivity().startService(intent);
        this.bindService();
        setServiceRunning(true);
    }

    private void bindService(){
        Intent intent = new Intent(getActivity(), LocationService.class);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void stopService(){
        Intent intent = new Intent(getActivity(), LocationService.class);
        getActivity().stopService(intent);
        getActivity().unbindService(mConnection);
        setServiceRunning(false);
    }


    private boolean isServiceStarted(){
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service: manager.getRunningServices(Integer.MAX_VALUE)){
            if(mServiceName.equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }
}
