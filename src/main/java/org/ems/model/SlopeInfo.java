package org.ems.model;

/**
 * Created by stas on 9/13/14.
 */
public class SlopeInfo {
    private final int avg;
    private final int max;
    private final int elevationGain;
    private final MatrixCoordinate firstPoint;

    public SlopeInfo(int avg, int max, int elevationGain, MatrixCoordinate firstPoint) {
        this.avg = avg;
        this.max = max;
        this.elevationGain = elevationGain;
        this.firstPoint = firstPoint;
    }

    public int getAvg() {
        return avg;
    }

    public int getMax() {
        return max;
    }

    public int getElevationGain() {
        return elevationGain;
    }

    public MatrixCoordinate getFirstPoint() {
        return firstPoint;
    }
}
