package org.ems.scanners;

import org.ems.model.Direction;
import org.ems.model.GeoCoordinate;
import org.ems.model.MatrixCoordinate;
import org.ems.model.Statistics;
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

    public Statistics diffForDirection(Direction dir, HGT hgt) {
        int[][] matrix = hgt.getHeightsMatrix();
        calcCellLengthForDirection(dir, hgt);
        //TODO: make matrix 1 longer (not -2)
        diffed = new int[matrix.length - 2][matrix[0].length - 2];
        Statistics statistics = new Statistics();
        for (int r = 1; r < matrix.length - 1; r++)
            for (int c = 1; c < matrix[r].length - 1; c++) {
                int secondPoint = matrix[r + dir.getRowShift()][c + dir.getColShift()];
                if (matrix[r][c] == VOID_VALUE || secondPoint == VOID_VALUE) {
                    diffed[r - 1][c - 1] = VOID_VALUE;
                    continue;
                }
//                statistics.addHeight(matrix[r][c], hgt.calcCoordinateForCell(c, r));
                int currDiff = matrix[r][c] - secondPoint;
//                statistics.addHeightDiff(currDiff);
                diffed[r - 1][c - 1] = currDiff;
            }
        return statistics;
    }

    private void calcCellLengthForDirection(Direction dir, HGT hgt) {
        cellLengthMeters = distanceMeters(hgt.getHeader().getCoordinate(),
                hgt.getHeader().getCoordinate().shift(-Math.abs(dir.getRowShift()), Math.abs(dir.getColShift()))) / hgt.getHeader().getHeight() - 1;
    }


    public Map<MatrixCoordinate, Integer> scanNotFixed(int minSteepnessAngle, int threshold, Direction direction) {
        double thresholdHeight = Math.tan(Math.toRadians(minSteepnessAngle)) * cellLengthMeters;
//        if (direction.getDiagonalRatio()) {
//            minSteepnessAngle = (int) Math.round(minSteepnessAngle * Math.sqrt(2)); //28
//        }

        Map<MatrixCoordinate, Integer> results = new HashMap<>();
        int heightSum;
        for (int r = 0; r < diffed.length; r++)
            for (int c = 0; c < diffed[r].length; c++) {
                heightSum = 0;
                for (int i = 0; ; i++) {
                    int ri = r + direction.getRowShift() * i;
                    int ci = c + direction.getColShift() * i;
                    if (ri >= 0 && ci >= 0 && ci < diffed[r].length && ri < diffed.length) {
                        int pointHeight = diffed[ri][ci];
                        if (pointHeight != VOID_VALUE) {
                            if (i == 0 || (heightSum + pointHeight) / (i + 1) >= thresholdHeight) {
                                heightSum += pointHeight;
                                continue;
                            }
                        }
                    }
                    break;
                }
                if (heightSum >= threshold) {
                    results.put(new MatrixCoordinate(c + 1, r + 1), heightSum);
                }
            }
        return results;
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
