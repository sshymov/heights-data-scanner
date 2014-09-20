package org.ems.scanners;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import junit.framework.TestCase;
import org.ems.model.Direction;
import org.ems.model.GeoCoordinate;
import org.ems.model.MatrixCoordinate;
import org.ems.model.SlopeInfo;
import org.ems.model.hgt.HGT;
import org.junit.Test;

public class ThresholdScannerTest extends TestCase {

    public static final GeoCoordinate SOME_COORDINATE = new GeoCoordinate(10, 10);


    @Test
    public void testDetectOnePointHeight() throws IOException {

        int[][] data = createFilledMatrix(10);
        data[1][1]=70;
        HGT hgt = HGT.create(SOME_COORDINATE, data);
        ThresholdScanner thresholdScanner = ThresholdScanner.createScanner(Direction.N, hgt);
        assertEquals(0,  thresholdScanner.scan(20, 61, null).size());
        assertEquals(1,  thresholdScanner.scan(20, 60, null).size());

        thresholdScanner = ThresholdScanner.createScanner(Direction.NW, hgt);
        assertEquals(0, thresholdScanner.scan(20, 61, null).size());
        assertEquals(1, thresholdScanner.scan(20, 60, null).size());
    }

    @Test
    public void testDetectFivePointHeight() throws IOException {
        int[][] data = createFilledMatrix(0);
        for (int i = 1; i <= 5; i++) {
            data[i][1] = i * 40;
        }
        ThresholdScanner thresholdScanner = ThresholdScanner.createScanner(Direction.N, HGT.create(SOME_COORDINATE, data));
        Map<MatrixCoordinate, SlopeInfo> result = thresholdScanner.scan(20, 100, null);
        assertEquals(1, result.size());
        SlopeInfo slopeInfo = result.get(new MatrixCoordinate(0, 5));
        assertNotNull(slopeInfo);
        assertEquals(5 * 40, slopeInfo.getElevationGain());
    }

    public int[][] createFilledMatrix(int value) {
        int[][] data = new int[1201][];
        int[] row = new int[1201];
        Arrays.fill(row, value);
        data[0] = row;
        for (int i = 1; i < 1201; i++) {
            data[i] = Arrays.copyOf(row, row.length);
        }
        return data;
    }

}