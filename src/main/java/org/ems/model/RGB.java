package org.ems.model;

/**
 * Created by stas on 6/21/14.
 */
public class RGB {
    public final int red,green,blue;

    public RGB(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public String toString() {
        return String.format("%02X%02X%02X", red, green, blue);
    }
}
