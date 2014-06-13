package org.ems.model;

import org.ems.model.Coordinate;
import org.ems.model.Latitude;
import org.ems.model.Longitude;
import org.junit.Assert;
import org.junit.Test;

public class CoordinateTest {

    @Test
    public void testCoordinateCalculation() {
        Coordinate coordinate = new Coordinate(new Longitude(47.0), new Latitude(30.0));
        Assert.assertEquals("N30.0,E47.0", coordinate.toString());
        Assert.assertEquals("N31.0,E48.0", coordinate.shift(1.0, 1.0).toString());

        Coordinate coordinate2 = new Coordinate(new Longitude(-147.0), new Latitude(-30.0));
        Assert.assertEquals(coordinate2.toString(), "S30.0,W147.0");
        Assert.assertEquals(coordinate2.shift(1.0, 1.0).toString(), "S29.0,W146.0");
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
