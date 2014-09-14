package org.ems.scanners;

import org.ems.model.*;
import org.ems.model.hgt.HGT;

import java.util.*;

/**
 * User: Stas Shimov <stas.shimov@gmail.com>
 * Date: Sep 27, 2008
 * Time: 3:50:16 PM
 */
public class ThresholdScanner {
    public static final int VOID_VALUE = -32768;
    private int[][] diffed;
    private double cellLengthMeters;

    public ThresholdScanner() {
    }

    public void diffForDirection(Direction dir, HGT hgt) {
        int[][] matrix = hgt.getHeightsMatrix();
        calcCellLengthForDirection(dir, hgt);
        //TODO: make matrix 1 longer (not -2)
        diffed = new int[matrix.length - 2][matrix[0].length - 2];
        for (int r = 1; r < matrix.length - 1; r++)
            for (int c = 1; c < matrix[r].length - 1; c++) {
                int secondPoint = matrix[r + dir.getRowShift()][c + dir.getColShift()];
                if (matrix[r][c] == VOID_VALUE || secondPoint == VOID_VALUE) {
                    diffed[r - 1][c - 1] = VOID_VALUE;
                    continue;
                }
                int currDiff = matrix[r][c] - secondPoint;
                diffed[r - 1][c - 1] = currDiff;
            }
    }

    private void calcCellLengthForDirection(Direction dir, HGT hgt) {
        GeoCoordinate oppositePoint = hgt.getHeader().getCoordinate().shift(Math.abs(dir.getColShift()), Math.abs(dir.getRowShift()));
        cellLengthMeters = distanceMeters(hgt.getHeader().getCoordinate(),oppositePoint) / (hgt.getHeader().getHeight() - 1);
    }


    public Map<MatrixCoordinate, SlopeInfo> scanNotFixed(int minSteepnessAngle, int threshold, Direction direction) {
        double thresholdSteepness = Math.tan(Math.toRadians(minSteepnessAngle)) * cellLengthMeters;

        Map<MatrixCoordinate, SlopeInfo> results = new HashMap<>();
        int heightSum;
        int cellNum;
        int maxSlope;
        MatrixCoordinate lastCoordinate;
        for (int r = 0; r < diffed.length; r++)
            for (int c = 0; c < diffed[0].length; c++) {
                heightSum = 0;
                maxSlope = 0;
                cellNum = 0;
                lastCoordinate=null;
                for (int i = 1; ; i++) {
                    int ri = r + direction.getRowShift() * i;
                    int ci = c + direction.getColShift() * i;
                    if (isInMatrix(ri, ci)) {
                        int pointHeight = diffed[ri][ci];
                        if (pointHeight != VOID_VALUE) {
                            if (i == 1 || (heightSum + pointHeight) / i >= thresholdSteepness) {
                                heightSum += pointHeight;
                                maxSlope=Math.max(maxSlope, calcAngle(pointHeight, cellLengthMeters));
                                cellNum=i;
                                lastCoordinate = new MatrixCoordinate(ci + direction.getColShift(), ri + direction.getRowShift() );
                                continue;
                            }
                        }
                    }
                    break;
                }
                if (heightSum >= threshold) {

                    //put new coordinate only if previous in the same direction doesn't exist or is lower then new one
                    MatrixCoordinate prevCoord = new MatrixCoordinate(c - Math.abs(direction.getColShift()),
                            r - Math.abs(direction.getRowShift()));
                    SlopeInfo prevValue = results.get(prevCoord);
                    SlopeInfo newSlopeInfo = new SlopeInfo(calcAngle(heightSum, cellNum*cellLengthMeters), maxSlope, heightSum, lastCoordinate);
                    if (prevValue!=null) {
                        if (prevValue.getElevationGain()<heightSum) {
                            results.remove(prevCoord);
//                            System.out.println("removed for "+direction);
                            results.put(new MatrixCoordinate(c, r), newSlopeInfo);
                        }
                    } else {
                        results.put(new MatrixCoordinate(c, r), newSlopeInfo);
                    }
                }
            }
        return results;
    }

    private int calcAngle(int height, double distance) {
        return (int)Math.round(Math.toDegrees(Math.atan(height/distance)));
    }

    private boolean isInMatrix(int ri, int ci) {
        return ri >= 0 && ci >= 0 && ci < diffed[0].length && ri < diffed.length;
    }

    public static long distanceMeters(GeoCoordinate a, GeoCoordinate b) {
        double earthRadius = 6353000;
        double dLat = Math.toRadians(b.getLatitude().getValue().subtract(a.getLatitude().getValue()).abs().doubleValue());
        double dLng = Math.toRadians(b.getLongitude().getValue().subtract(a.getLongitude().getValue()).abs().doubleValue());
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double res = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(a.getLatitude().getValue().doubleValue())) * Math.cos(Math.toRadians(b.getLatitude().getValue().doubleValue()));
        double c = 2 * Math.atan2(Math.sqrt(res), Math.sqrt(1 - res));

        return Math.round(earthRadius * c);
    }
}
