package org.ems.model;

import java.math.BigDecimal;

/**
 * North to South position
 *
 * User: Stas Shimov <stas.shimov@gmail.com>
 * Date: Oct 16, 2007
 * Time: 9:30:03 PM
 */
public class Latitude extends GeoDimention {
    public final static int LATITUDE_LENGTH=180;



    public Latitude(BigDecimal value) {
        super(value, LATITUDE_LENGTH);
    }

    public Latitude(double value) {
        super(BigDecimal.valueOf(value), LATITUDE_LENGTH);
    }

    @Override
    public Hemisphere getHemisphere()    {
        return (getValue().compareTo(BigDecimal.ZERO)>=0)?Hemisphere.North:Hemisphere.South;
    }
}
