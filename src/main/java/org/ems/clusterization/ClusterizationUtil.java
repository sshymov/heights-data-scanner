package org.ems.clusterization;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by stas on 10/28/14.
 */
public final class ClusterizationUtil {

    private ClusterizationUtil() {
    }

    public static <T extends MeasurableDistance> List<List<T>> clusterize(List<T> points, double limit) {
        List<List<T>> clusters = new ArrayList<>();

        while (points.size() > 0) {
            T point = points.remove(0);
            List<T> cluster = walkNearbyPoints(points, limit, point);
            cluster.add(point);
            clusters.add(cluster);
        }
        return clusters;
    }

    private static <T extends MeasurableDistance> List<T> walkNearbyPoints(List<T> points, double limit, T point) {
        List<T> nearbyPoints = new ArrayList<>();
        for (ListIterator<T> it = points.listIterator(); it.hasNext(); ) {
            T nearbyPoint = it.next();
            if (point.distanceTo(nearbyPoint) <= limit) {
                it.remove();
                nearbyPoints.add(nearbyPoint);
            }
        }
        for (ListIterator<T> it = new ArrayList<>(nearbyPoints).listIterator(); it.hasNext() && !points.isEmpty(); ) {
            nearbyPoints.addAll(walkNearbyPoints(points, limit, it.next()));
        }

        return nearbyPoints;
    }
}
