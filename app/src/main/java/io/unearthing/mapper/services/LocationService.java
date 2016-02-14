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

package io.unearthing.mapper.services;

import android.support.v4.app.NotificationCompat;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.widget.Toast;

import io.unearthing.mapper.R;
import io.unearthing.mapper.ui.activities.MainActivity;
import io.unearthing.mapper.tracker.FusedLocationManager;
import io.unearthing.mapper.tracker.Tracker;
import io.unearthing.mapper.ILocationService;
public class LocationService extends Service {
    int mStartMode =  Service.START_NOT_STICKY;       // indicates how to behave if the service is killed
    boolean mAllowRebind; // indicates whether onRebind should be used
    private Looper mServiceLooper;
    private boolean started = false;
    private FusedLocationManager fusedLocationManager;
    private Tracker tracker;

    private final ILocationService.Stub mBinder = new ILocationService.Stub(){

        @Override
        public String status() throws RemoteException {
            return null;
        }

        @Override
        public void start() throws RemoteException {

        }
    };

    @Override
    public void onCreate() {

        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        Context ctx = (Context) this;
        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();

        this.tracker = new Tracker(ctx);
        this.fusedLocationManager = new FusedLocationManager(tracker, ctx);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Mapper")
                .setContentText("Currently tracking your location")
                .setSmallIcon(R.drawable.ic_media_play)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
        startForeground(2, notification);
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(started) {
            stopSelf();
        } else {
            fusedLocationManager.start(tracker);
            started = true; // move to location manager code
        }

        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        return mBinder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return mAllowRebind;
    }
    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }

    @Override
    public void onDestroy() {
        if(started){
            fusedLocationManager.stop();
        }
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

}