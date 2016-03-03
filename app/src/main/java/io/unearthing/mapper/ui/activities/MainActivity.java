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
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import io.unearthing.mapper.R;
import io.unearthing.mapper.ui.fragments.TripListFragment;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    protected DrawerLayout mDrawerLayout;
    protected Class currentFragment = null;
    protected final String[] drawerOptions = {"Trips"};
    private final static int REQUEST_FINE_LOCATION_PERMISSION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setupDrawer();
        ListView drawer = (ListView) this.findViewById(R.id.left_drawer);
        drawer.setOnItemClickListener(this);
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
        int res = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(res == PackageManager.PERMISSION_GRANTED) {
            selectItem(0);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_FINE_LOCATION_PERMISSION);

        }
    }

    protected void setupDrawer(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, drawerOptions));
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position);
    }

    protected void selectItem(int position) {
        switch (position) {
            case 0:
                switchFragment(TripListFragment.class);
                break;
        }
        mDrawerLayout.closeDrawers();
    }

    protected void switchFragment(Class klass){
        if(currentFragment != klass){
            currentFragment = klass;
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = null;
            try{
                fragment = (Fragment) klass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            fragmentTransaction.replace(R.id.content_frame, (Fragment) fragment);
            fragmentTransaction.commit();
        }
    }
}

