package org.ems.scanners;

/**
* Created by stas on 25.05.14.
 *  start index 1st cicle, 2nd cicle
 N 1 0 m[k][t]-m[k-1][t]
 S 1 0 m[k][t]-m[k+1][t])
 E 0 1 m[k][t]-m[a][1]
 W 0 1 m[k][t]-m[a][1]
*/
public enum Direction {N("North", -1,0),NE("North-East", -1,1),E("East", 0, 1),SE("South-East", 1,1),

    S("South", 1,0),SW("South-West", 1,-1),W("West", 0,-1),NW("North-West", -1,-1);
    private final String title;
    private int rowShift,colShift;
    Direction(String title, int rowShift,int colShift) {
        this.title=title;
        this.rowShift=rowShift;
        this.colShift=colShift;
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
}
