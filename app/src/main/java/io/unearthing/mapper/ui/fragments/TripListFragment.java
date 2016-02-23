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

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
import io.unearthing.mapper.R;
import io.unearthing.mapper.model.Trip;
import io.unearthing.mapper.model.helpers.LocationDbLocal;
import io.unearthing.mapper.services.UploadTrip;
import io.unearthing.mapper.ui.activities.MapActivity;

public class TripListFragment extends Fragment {

    public TripListFragment() {

    }

    /*
        LIFE CYCLE OVERRIDES
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup parentVw = container;
        View view = inflater.inflate(R.layout.fragment_trip_list, container, false);
        LocationDbLocal db = new LocationDbLocal(this.getContext());
        Cursor c = db.getTrips();
        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(getContext(),
                R.layout.trip_list_item,
                c,
                new String[] { "title", "_id"},//,"count"
                new int[] { R.id.trip_title,R.id.location_count },//,
                0);
        ListView list = (ListView) view.findViewById(R.id.trip_list);
        list.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openPopup(id);
            }
        });
        list.setAdapter(listAdapter);
        return view;
    }

    public void openPopup(final long tripId){
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.trip_pop_up, null, false);
        final PopupWindow pw = new PopupWindow(
                view,
                300,
                300,
                true);
        view.findViewById(R.id.sync_trip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Trip trip = new Trip(getContext());
                trip.find(tripId);
                UploadTrip uploader = new UploadTrip();
                uploader.execute(trip);
            }
        });
        view.findViewById(R.id.view_trip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                intent.putExtra("trip", tripId);
                pw.dismiss();
                startActivity(intent);
            }
        });
        // The code below assumes that the root container has an id called 'main'
        pw.showAtLocation(getActivity().findViewById(R.id.content_frame), Gravity.CENTER, 0, 0);
        }
    }
