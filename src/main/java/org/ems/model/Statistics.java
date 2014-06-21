package org.ems.model;

public class Statistics {
    private int minHeight;
    private int maxHeight;
    private GeoCoordinate minHeightCoordinate;
    private GeoCoordinate maxHeightCoordinate;
    private long heightSum;
    private int pointsNum;
    private int maxDiff;
    private long diffSum;
    private int pointsDiffNum;

    public void addHeight(int height, GeoCoordinate coordinate) {
        if (height>maxHeight) {
            maxHeightCoordinate=coordinate;
            maxHeight=height;
        }
        if (height<minHeight) {
            minHeightCoordinate=coordinate;
            minHeight=height;

        }
        heightSum+=height;
        pointsNum++;
    }
    public void addHeightDiff(int heightDiff) {
        if (heightDiff>maxDiff) {
            maxDiff=heightDiff;
        }
        diffSum+=heightDiff;
        pointsDiffNum++;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "minHeight=" + minHeight +
                ", maxHeight=" + maxHeight +
                ", minHeightCoordinate=" + minHeightCoordinate +
                ", maxHeightCoordinate=" + maxHeightCoordinate +
                ", heightAvg=" + heightSum/pointsNum +
                ", maxDiff=" + maxDiff +
                ", diffAvg=" + diffSum/pointsDiffNum +
                '}';
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public GeoCoordinate getMinHeightCoordinate() {
        return minHeightCoordinate;
    }

    public GeoCoordinate getMaxHeightCoordinate() {
        return maxHeightCoordinate;
    }
}
