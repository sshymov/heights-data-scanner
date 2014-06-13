package org.ems.tests.model;

import junit.framework.TestCase;
import org.ems.model.Latitude;
import org.ems.model.Longitude;

/**
 * User: Stas Shimov <stas.shimov@gmail.com>
 * Date: Sep 27, 2008
 * Time: 12:53:30 PM
 */
public class LongitudeTest extends TestCase {
    public void testInputFormat() {
        Longitude longitude = new Longitude(34.0264);
        assertEquals(34.0264, longitude.getValue());

        //another hemisphere
        longitude = new Longitude(-66.106);
        assertEquals(-66.106, longitude.getValue());

    }
}
