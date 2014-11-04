package org.ems.clusterization;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Created by stas on 10/28/14.
 */
public class ClusterizationUtilTest {

    private static class Point implements MeasurableDistance<Point> {
        private int x;

        private Point(int x) {
            this.x = x;
        }

        @Override
        public double distanceTo(Point other) {
            return Math.abs(x - other.x);
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    '}';
        }
    }

    @Test
    public void testClusterizationForOnePoint() {
        Point point = new Point(10);
        List<Set<Point>> clusters = ClusterizationUtil.clusterize(Lists.newArrayList(point), 2);

        assertEquals(1, clusters.size());
        assertEquals(1, clusters.get(0).size());
        assertSame(clusters.get(0).iterator().next(), point);
    }

    @Test
    public void testClusterizationForTwoClusters() {
        List<Set<Point>> clusters = ClusterizationUtil.clusterize(
                Lists.newArrayList(new Point(1), new Point(3), new Point(10), new Point(11), new Point(12), new Point(2)), 1);

        assertEquals(2, clusters.size());
        assertEquals(3, clusters.get(0).size());
        assertEquals(3, clusters.get(1).size());
    }

    @Test
    public void testClusterizationForPointWithSpaces() {
        List<Set<Point>> clusters = ClusterizationUtil.clusterize(
                Lists.newArrayList(new Point(1), new Point(3), new Point(9), new Point(5), new Point(11), new Point(7)), 2);

        assertEquals(1, clusters.size());
        assertEquals(6, clusters.get(0).size());
    }
}
