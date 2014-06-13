package org.ems.model.hgt;

import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * User: Stas Shimov <stas.shimov@gmail.com>
 * Date: Oct 15, 2007
 * Time: 2:20:35 AM
 */
public class HeaderTest {

    @Test
    public void testHeaderCreation() {
        try {
            Header h=new Header("N48E030.dem",2884802L);
            assertEquals(1201,h.getWidth());
            assertEquals(h.getHeight(),h.getWidth());
            assertEquals("N",h.getCoordinate().getLatitude().getHemisphereAsString());
            assertEquals("E",h.getCoordinate().getLongitude().getHemisphereAsString());
            assertEquals(48,h.getCoordinate().getLatitude().getDegrees());
            assertEquals(30,h.getCoordinate().getLongitude().getDegrees());
        } catch (ParseException e) {
            fail("Should parse Ok, problem:"+e.getMessage());
        }
        //       N00E072.hgt.zip    S10W158.hgt.zip  	

        try {
            new Header("N48E030.dem",2884802L);
        } catch (ParseException e) {
            fail("Should parse Ok, problem:"+e.getMessage());
        }
    }
}
