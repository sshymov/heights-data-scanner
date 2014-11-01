package org.ems.model;

/**
 * Created by stas on 9/13/14.
 */
public class SlopeInfo {
    private final int avg;
    private final int max;
    private final int elevationGain;
    private final MatrixCoordinate lowPoint;
    private final MatrixCoordinate highPoint;
    private final Direction direction;

    public SlopeInfo(Direction direction, MatrixCoordinate highPoint, int avg, int max, int elevationGain, MatrixCoordinate lowPoint ) {
        this.avg = avg;
        this.max = max;
        this.elevationGain = elevationGain;
        this.lowPoint = lowPoint;
        this.highPoint = highPoint;
        this.direction = direction;
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

    public MatrixCoordinate getLowPoint() {
        return lowPoint;
    }

    public MatrixCoordinate getHighPoint() {
        return highPoint;
    }

    public Direction getDirection() {
        return direction;
    }
}
