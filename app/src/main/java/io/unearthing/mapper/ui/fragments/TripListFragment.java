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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;

import java.util.List;

import io.unearthing.mapper.CloudentBuilder;
import io.unearthing.mapper.R;
import io.unearthing.mapper.model.Trip;
import io.unearthing.mapper.model.helpers.LocationDbLocal;
import io.unearthing.mapper.services.UploadTrip;
import io.unearthing.mapper.ui.TripAdapter;
import io.unearthing.mapper.ui.activities.MapActivity;

public class TripListFragment extends Fragment {

    List<Trip> mTrips;

    public TripListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup parentVw = container;
        View view = inflater.inflate(R.layout.fragment_trip_list, container, false);
        mTrips =new Trip(getContext()).findAll(getContext());
        TripAdapter adapter = new TripAdapter(mTrips, getActivity());
        RecyclerView mRecyclerView = (RecyclerView)view.findViewById(R.id.trip_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);
        return view;
    }
}
