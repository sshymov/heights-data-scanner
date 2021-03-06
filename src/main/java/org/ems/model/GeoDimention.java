package org.ems.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * User: Stas Shimov <stas.shimov@gmail.com>
 * Date: Oct 15, 2007
 * Time: 12:55:22 AM
 */
public abstract class GeoDimention {
    private final BigDecimal value;

    public enum Hemisphere {
        North, South, East, West;

        public String getShort() {
            return name().substring(0, 1);
        }

        public Hemisphere findHemisphere(String shortName) {
            for (Hemisphere h : values()) {
                if (h.getShort().equals(shortName))
                    return h;
            }
            throw new IllegalArgumentException("Unknown hemisphere short name: " + shortName);
        }
    }


    public GeoDimention(BigDecimal value, int length) {
        int maxValue = length / 2;
        int minValue = -maxValue;
        if (value.intValue() > maxValue || value.intValue() < minValue)
            throw new IllegalArgumentException("GeoCoordinate value is out of range");
        this.value = value;
    }


    abstract public Hemisphere getHemisphere();

    public String getHemisphereAsString() {
        return getHemisphere().getShort();
    }

    public int getDegrees() {
        return value.abs().intValue();
    }


    public BigDecimal getValue() {
        return value;
    }

    public String toString() {
        return getHemisphereAsString() +
                String.valueOf(value.abs().setScale(value.scale()<5?value.scale():5, RoundingMode.HALF_UP));
    }
}
