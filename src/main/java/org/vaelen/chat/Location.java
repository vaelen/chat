/*
Chat, a simple chat application.
Copyright (C) 2017, Andrew Young <andrew@vaelen.org>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.vaelen.chat;

import java.util.Arrays;
import java.util.List;

public class Location {

    private String type = "Point";
    private List<Double> coordinates = Arrays.asList(0d,0d);

    public Location() {
    }

    public Location(double longitude, double latitude) {
        this.coordinates = Arrays.asList(longitude, latitude);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLongitude() {
        return coordinates.get(0);
    }

    public double getLatitude() {
        return coordinates.get(1);
    }

}
