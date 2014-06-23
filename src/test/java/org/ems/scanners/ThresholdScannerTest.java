package org.ems.scanners;

import java.io.IOException;
import java.util.Map;

import com.google.common.base.Function;
import junit.framework.TestCase;
import org.ems.model.Direction;
import org.ems.model.GeoCoordinate;
import org.ems.model.MatrixCoordinate;
import org.ems.model.hgt.HGT;
import org.junit.Test;

public class ThresholdScannerTest extends TestCase {

    public static final GeoCoordinate SOME_COORDINATE = new GeoCoordinate(10, 10);
    public static final Function<MatrixCoordinate, MatrixCoordinate> CONVERTER = new Function<MatrixCoordinate, MatrixCoordinate>() {

        @Override
        public MatrixCoordinate apply(MatrixCoordinate matrixCoordinate) {
            return matrixCoordinate;
        }
    };


    @Test
    public void testDetectOnePointHeight() throws IOException {
        int[][] data = {
                { 10, 10, 10 },
                { 10, 70, 10 },
                { 10, 10, 10 } };
        ThresholdScanner thresholdScanner = new ThresholdScanner();
        thresholdScanner.diffForDirection(Direction.N, HGT.create(SOME_COORDINATE, data));
        assertEquals(0,  thresholdScanner.scanNotFixed(20, 61, Direction.N, CONVERTER).size());
        assertEquals(1,  thresholdScanner.scanNotFixed(20, 60, Direction.N, CONVERTER).size());

        thresholdScanner.diffForDirection(Direction.NE, HGT.create(SOME_COORDINATE, data));
        assertEquals(0, thresholdScanner.scanNotFixed(20, 61, Direction.NE, CONVERTER).size());
        assertEquals(1, thresholdScanner.scanNotFixed(20, 60, Direction.NE, CONVERTER).size());
    }

    @Test
    public void testDetectFivePointHeight() throws IOException {
        int i = 10;
        int[][] data = {
                { 10, 10, 10 },
                { 10, i += 20, 10 },
                { 10, i += 20, 10 },
                { 10, i += 20, 10 },
                { 10, i += 20, 10 },
                { 10, i += 20, 10 },
                { 10, 10, 10 } };
        ThresholdScanner thresholdScanner = new ThresholdScanner();
        thresholdScanner.diffForDirection(Direction.N, HGT.create(SOME_COORDINATE, data));
        Map<MatrixCoordinate, Integer> result = thresholdScanner.scanNotFixed(20, 100, Direction.N, CONVERTER);
        assertEquals(1, result.size());
        assertEquals(100, (int) result.get(new MatrixCoordinate(0, 4)));
    }
}