package org.ems.scanners;

import com.google.common.base.Function;
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
    private HGT hgt;

    public ThresholdScanner(HGT hgt) {
        this.hgt = hgt;
    }

//    public Map<Direction,Integer> processPoint(int x, int y, int[][] matrix, int threshold) {
//        int centre=matrix[y][x];
//        if (centre==VOID_VALUE) {
//            System.out.println("Void data detected skipping");
//            return null;
//        }
//        Map<Direction,Integer> result=new LinkedHashMap<Direction,Integer>();
//        for (Direction dir : Direction.values()) {
//            if (matrix[y+dir.getY()][x+dir.getX()]==VOID_VALUE)
//                continue;
//            int diff=centre-matrix[y+dir.getY()][x+dir.getX()];
//            if (diff>threshold) {
//                result.put(dir,diff);
//            }
//        }
//        return (result.size()>0)?result:null;
//    }

    public Statistics diffForDirection(Direction dir) {
        int[][] matrix = hgt.getHeightsMatrix();
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
                statistics.addHeight(matrix[r][c], hgt.calcCoordinateForCell(c, r));
                int currDiff = matrix[r][c] - secondPoint;
                statistics.addHeightDiff(currDiff);
                diffed[r - 1][c - 1] = currDiff;
            }
        return statistics;
    }

    public <T> Map<T, Integer> scan(int pointsNumber, int threshold, Direction direction, Function<MatrixCoordinate,T> converter) {
//        GeoCoordinate cellCoordinate=hgt.getHeader().getCoordinate();
        Map<T, Integer> results = new HashMap<>();
        int sum;
        for (int r = 0; r < diffed.length; r++)
            for (int c = 0; c < diffed[r].length; c++) {
                sum=0;
                for (int i=0; i<pointsNumber;i++) {
                    int ri = r + direction.getRowShift() * i;
                    int ci = c + direction.getColShift() * i;
                    if (ri >= 0 && ci >= 0 && ci < diffed[r].length && ri < diffed.length) {
                        int pointHeight = diffed[ri][ci];
                        if (pointHeight != VOID_VALUE) {
                            sum += pointHeight;
                        }
                    }
                }
                if (sum>threshold) {

                    results.put(converter.apply(new MatrixCoordinate(c, r)), sum);
                }
            }
        return results;
    }
}
