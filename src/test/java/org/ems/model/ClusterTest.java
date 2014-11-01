package org.ems.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by stas on 10/16/14.
 */
public class ClusterTest {
    @Test
    public void testPointExtendsTheCluster() {
        Cluster cluster = new Cluster();
        MatrixCoordinate p77 = new MatrixCoordinate(7, 7);
        cluster.add( new SlopeInfo(Direction.E, p77, 0, 0, 0, null));
        MatrixCoordinate p66 = new MatrixCoordinate(6, 6);
        cluster.add( new SlopeInfo(Direction.E, p66, 0, 0, 0, null));
        cluster.add(new SlopeInfo(Direction.NE, p66, 0, 0, 0, null));
        assertEquals(2, cluster.getSize());
        assertEquals(2, cluster.getDirectionCoordinates().size());
        assertEquals(2, cluster.getDirectionCoordinates().get(Direction.E).size());
    }
}
