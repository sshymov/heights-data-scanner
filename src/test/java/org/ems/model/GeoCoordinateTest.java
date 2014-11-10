package org.ems.model;

import org.junit.Assert;
import org.junit.Test;

public class GeoCoordinateTest {

    @Test
    public void testCoordinateCalculation() {
        GeoCoordinate coordinate = new GeoCoordinate(new Longitude(47.0), new Latitude(30.0));
        Assert.assertEquals("E47.0,N30.0", coordinate.toString());
        Assert.assertEquals("E49.0,N31.0", coordinate.shift(2.0, 1.0).toString());

        GeoCoordinate coordinate2 = new GeoCoordinate(new Longitude(-147.0), new Latitude(-30.0));
        Assert.assertEquals(coordinate2.toString(), "W147.0,S30.0");
        Assert.assertEquals(coordinate2.shift(1.0, 1.0).toString(), "W146.0,S29.0");
    }

    @Test
    public void testCoordinateRound() {
        GeoCoordinate coordinate = new GeoCoordinate(new Longitude(47.1234567), new Latitude(30.1234567));
        Assert.assertEquals("E47.12346,N30.12346", coordinate.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalLon() {
        new GeoCoordinate(new Longitude(181.0), new Latitude(30.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalLat() {
        new GeoCoordinate(new Longitude(100.0), new Latitude(91.0));
    }

}
