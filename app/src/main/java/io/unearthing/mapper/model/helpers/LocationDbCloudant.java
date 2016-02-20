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

package io.unearthing.mapper.model.helpers;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;

import io.unearthing.mapper.model.Location;

public class LocationDbCloudant implements LocationDb {
    private CloudantClient db;

    public LocationDbCloudant(String username, String password, String account){
        db = ClientBuilder.account(account)
                .username(username)
                .password(password)
                .build();

    }

    @Override
    public int countTripLocations() {
        return 0;
    }

    @Override
    public int countTrips() {
        return 0;
    }

    @Override
    public void clearDatabase() {

    }

    @Override
    public int deleteSession(long id) {
        return 0;
    }

    @Override
    public int deleteSessionLocations(long sessionId) {
        return 0;
    }

    @Override
    public int[] addBulkLocations(Location[] locations) {
        return new int[0];
    }

}
