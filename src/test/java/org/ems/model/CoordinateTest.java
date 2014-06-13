package org.ems.model;

import org.junit.Assert;
import org.junit.Test;

public class CoordinateTest {

    @Test
    public void testCoordinateCalculation() {
        Coordinate coordinate = new Coordinate(new Longitude(47.0), new Latitude(30.0));
        Assert.assertEquals("E47.0,N30.0", coordinate.toString());
        Assert.assertEquals("E48.0,N31.0", coordinate.shift(1.0, 1.0).toString());

        Coordinate coordinate2 = new Coordinate(new Longitude(-147.0), new Latitude(-30.0));
        Assert.assertEquals(coordinate2.toString(), "W147.0,S30.0");
        Assert.assertEquals(coordinate2.shift(1.0, 1.0).toString(), "W146.0,S29.0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalLon() {
        new Coordinate(new Longitude(181.0), new Latitude(30.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalLat() {
        new Coordinate(new Longitude(100.0), new Latitude(91.0));
    }

}
