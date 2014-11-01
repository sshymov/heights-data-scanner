package org.ems.scanners;

import org.ems.model.Direction;
import org.ems.model.GeoCoordinate;
import org.ems.model.MatrixCoordinate;
import org.ems.model.SlopeInfo;
import org.ems.model.hgt.HGT;

import java.util.*;

/**
 * User: Stas Shimov <stas.shimov@gmail.com>
 * Date: Sep 27, 2008
 * Time: 3:50:16 PM
 */
public class ThresholdScanner {
    public static final int VOID_VALUE = -32768;
    private final Direction direction;
    private int[][] diffed;
    private double cellLengthMeters;

    private ThresholdScanner(Direction dir, int[][] diffed, double cellLengthMeters) {
        this.direction = dir;
        this.diffed = diffed;
        this.cellLengthMeters = cellLengthMeters;
    }

    public static ThresholdScanner createScanner(Direction dir, HGT hgt) {
        return new ThresholdScanner(dir, diffForDirection(dir, hgt), calcCellLengthForDirection(dir, hgt));
    }

    private static int[][] diffForDirection(Direction dir, HGT hgt) {
        int[][] matrix = hgt.getHeightsMatrix();

        //TODO: make matrix 1 longer (not -2)
        int[][] diffed = new int[matrix.length - 2][matrix[0].length - 2];
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
        return diffed;
    }

    private static long calcCellLengthForDirection(Direction dir, HGT hgt) {
        GeoCoordinate oppositePoint = hgt.getHeader().getCoordinate().shift(Math.abs(dir.getColShift()), Math.abs(dir.getRowShift()));
        return distanceMeters(hgt.getHeader().getCoordinate(), oppositePoint) / (hgt.getHeader().getHeight() - 1);
    }


    public Collection<SlopeInfo> scan(int minSteepnessAngle, int threshold, Integer minMaxSlope) {
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
                lastCoordinate = null;
                for (int i = 0; ; i++) {
                    int ri = r + direction.getRowShift() * i;
                    int ci = c + direction.getColShift() * i;
                    if (isInMatrix(ri, ci)) {
                        int pointHeight = diffed[ri][ci];
                        if (pointHeight != VOID_VALUE) {
                            if (i == 0 || (heightSum + pointHeight) / (i + 1) >= thresholdSteepness) {
                                heightSum += pointHeight;
                                maxSlope = Math.max(maxSlope, calcAngle(pointHeight, cellLengthMeters));
                                cellNum = i + 1;
                                lastCoordinate = new MatrixCoordinate(ci + direction.getColShift(), ri + direction.getRowShift());
                                continue;
                            }
                        }
                    }
                    break;
                }
                if (heightSum >= threshold && (minMaxSlope == null || maxSlope >= minMaxSlope)) {

                    MatrixCoordinate highPoint = new MatrixCoordinate(c - direction.getColShift(), r - direction.getRowShift());
                    SlopeInfo newSlopeInfo = new SlopeInfo(direction, highPoint,
                            calcAngle(heightSum, cellNum * cellLengthMeters), maxSlope,
                            heightSum, lastCoordinate);
                    results.put(highPoint, newSlopeInfo);
                }
            }
        filterOutSequentialPoints(results);
        return results.values();
    }

    private void filterOutSequentialPoints(Map<MatrixCoordinate, SlopeInfo> coordinates) {
        List<MatrixCoordinate> prevCoordinates = new ArrayList<>();
        for (MatrixCoordinate coordinate : coordinates.keySet()) {
            MatrixCoordinate prevCoord = new MatrixCoordinate(coordinate.x + direction.getColShift(),
                    coordinate.y + direction.getRowShift());
            SlopeInfo prevValue = coordinates.get(prevCoord);
            if (prevValue != null && prevValue.getElevationGain() < coordinates.get(coordinate).getElevationGain()) {
                prevCoordinates.add(prevCoord);
            }
        }
        for (MatrixCoordinate matrixCoordinate : prevCoordinates) {
            coordinates.remove(matrixCoordinate);
        }
    }

    private int calcAngle(int height, double distance) {
        return (int) Math.round(Math.toDegrees(Math.atan(height / distance)));
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
