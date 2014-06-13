package org.ems.model;

import java.math.BigDecimal;

import org.ems.model.GeoDimention.Hemisphere;

/**
 * East to West position
 * User: Stas Shimov <stas.shimov@gmail.com>
 * Date: Oct 16, 2007
 * Time: 9:29:55 PM
 */
public class Longitude  extends GeoDimention{
    public final static int LONGITUDE_LENGTH=360;



    public Longitude(BigDecimal value) {
        super(value, LONGITUDE_LENGTH);
    }

    public Longitude(double value) {
        super(BigDecimal.valueOf(value),  LONGITUDE_LENGTH);
    }



    @Override
    public Hemisphere getHemisphere() {
        return (getValue().compareTo(BigDecimal.ZERO)>=0)?Hemisphere.East:Hemisphere.West;
    }
}
