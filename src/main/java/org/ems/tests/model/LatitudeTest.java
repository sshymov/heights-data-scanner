package org.ems.tests.model;

import junit.framework.TestCase;
import org.ems.model.Latitude;

/**
 * User: Stas Shimov <stas.shimov@gmail.com>
 * Date: Sep 27, 2008
 * Time: 12:53:23 PM
 */
public class LatitudeTest extends TestCase {
    public void testInputFormat() {
        Latitude lat=new Latitude(45.5091);
        assertEquals(45.5091,lat.getValue());


        //another hemisphere
        lat=new Latitude(-39.5714);
        assertEquals(-39.5714,lat.getValue());


    }
}
