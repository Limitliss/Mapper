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

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import io.unearthing.mapper.R;
import io.unearthing.mapper.model.LocationDbLocal;
import io.unearthing.mapper.model.definitions.TripTableHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatabaseFragment extends Fragment {

    LocationDbLocal db;
    TextView recordCountLabel;
    long tripID;
    public DatabaseFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_database, container, false);
        db = new LocationDbLocal(this.getContext());
        view.findViewById(R.id.add_row).setOnClickListener(this.addRow());
        view.findViewById(R.id.count_rows).setOnClickListener(this.countRows());
        view.findViewById(R.id.clear_database).setOnClickListener(this.clearDatabase());
        view.findViewById(R.id.addTrip).setOnClickListener(this.addTrip());
        view.findViewById(R.id.count_trips).setOnClickListener(this.countTrips());
        recordCountLabel = (TextView) view.findViewById(R.id.records_label);
        return view;
    }

    public View.OnClickListener addRow(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addLocation(1, 1, 1,tripID);
            }
        };
    }

    public View.OnClickListener addTrip(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripID = db.startSession();
            }
        };

    }

    public View.OnClickListener countRows(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rows = db.countRows();
                recordCountLabel.setText(rows + " Records");
            }
        };
    }

    public View.OnClickListener clearDatabase(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.clearDatabase();
            }
        };
    }
    public View.OnClickListener countTrips(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = db.getTrips();
                SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(getContext(),
                        R.layout.trip_list_item,
                        c,
                        new String[] { "title"},//,"count"
                        new int[] { R.id.trip_title });//, R.id.location_count
            }
        };
    }
}
