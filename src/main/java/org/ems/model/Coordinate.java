package org.ems.model;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Stas Shimov <stas.shimov@gmail.com>
 * Date: Oct 15, 2007
 * Time: 12:50:45 AM
 */
public class Coordinate {
    private final Latitude latitude; //0,+90 N, 0,-90 S
    private final Longitude longitude; //0,+180 E, 0,-180 W


    public Coordinate(Coordinate coordinate) {
        latitude=coordinate.getLatitude();
        longitude=coordinate.getLongitude();
    }

    public Coordinate(Longitude longitude, Latitude latitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public Latitude getLatitude() {
        return latitude;
    }


    public Longitude getLongitude() {
        return longitude;
    }


    public String toString() {
        return longitude.toString()+","+latitude.toString();
    }
    public String toDigitsString() {
        return longitude.getValue()+","+latitude.getValue();
    }

    public Coordinate shift(double lon, double lat) {
        return new Coordinate(new Longitude(this.getLongitude().getValue().add(BigDecimal.valueOf(lon))),
                new Latitude(this.getLatitude().getValue().add(BigDecimal.valueOf(lat))));
    }
}
