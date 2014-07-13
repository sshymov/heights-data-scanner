package org.ems.model;

import org.ems.model.RGB;

/**
 * Created by stas on 25.05.14.
 */
public enum Direction {
    N("North", -1, 0, new RGB(0, 0, 255), false),
    NE("North-East", -1, 1, new RGB(0, 165, 255), true),
    E("East", 0, 1, new RGB(0, 255, 0), false),
    SE("South-East", 1, 1, new RGB(173, 255, 47), true),
    S("South", 1, 0, new RGB(255, 0, 0), false),
    SW("South-West", 1, -1, new RGB(127, 255, 212), true),
    W("West", 0, -1, new RGB(0, 255, 255), false),
    NW("North-West", -1, -1, new RGB(106, 90, 205), true);
    private final String title;
    private final int rowShift, colShift;
    private final RGB rgbColor;
    private final boolean diagonalRatio;

    Direction(String title, int rowShift, int colShift, RGB rgb, boolean diagonalRatio) {
        this.title = title;
        this.rowShift = rowShift;
        this.colShift = colShift;
        this.rgbColor = rgb;
        this.diagonalRatio = diagonalRatio;
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

    public boolean getDiagonalRatio() {
        return diagonalRatio;
    }
}
