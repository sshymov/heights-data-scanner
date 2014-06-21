package org.ems.scanners;

import org.ems.model.RGB;
import sun.plugin.dom.css.RGBColor;

/**
* Created by stas on 25.05.14.
 *  start index 1st cicle, 2nd cicle
 N 1 0 m[k][t]-m[k-1][t]
 S 1 0 m[k][t]-m[k+1][t])
 E 0 1 m[k][t]-m[a][1]
 W 0 1 m[k][t]-m[a][1]
*/
public enum Direction {
    N("North", -1,0, new RGB(255,0,0)),
    NE("North-East", -1,1, new RGB(255,165,0)),
    E("East", 0, 1, new RGB(255,255,0)),
    SE("South-East", 1,1, new RGB(173,255,47)),
    S("South", 1,0, new RGB(0,255,0)),
    SW("South-West", 1,-1, new RGB(127,255,212)),
    W("West", 0,-1, new RGB(0,0,255)),
    NW("North-West", -1,-1, new RGB(106,90,205));
    private final String title;
    private int rowShift,colShift;
    private RGB rgbColor;
    Direction(String title, int rowShift,int colShift, RGB rgb) {
        this.title=title;
        this.rowShift=rowShift;
        this.colShift=colShift;
        this.rgbColor=rgb;
    }

    public int getRowShift() {
        return rowShift;
    }

    public int getColShift() {
        return colShift;
    }

    public String getTitle() {
        return title;
    }

    public RGB getRgbColor() {
        return rgbColor;
    }
}
