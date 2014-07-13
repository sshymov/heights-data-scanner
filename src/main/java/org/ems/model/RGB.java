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

    public String toKml() {
        //abgr - kml format
        return String.format("ff%02X%02X%02X", blue, green, red);
    }
}
