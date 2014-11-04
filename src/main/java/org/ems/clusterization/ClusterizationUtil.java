package org.ems.clusterization;

import java.util.*;

/**
 * Created by stas on 10/28/14.
 */
public final class ClusterizationUtil {

    private ClusterizationUtil() {
    }

    public static <T extends MeasurableDistance> List<Set<T>> clusterize(List<T> points, double limit) {
        List<Set<T>> clusters = new ArrayList<>();
        LinkedList<T> pointsCopy=new LinkedList<>(points);
        while (pointsCopy.size() > 0) {
            T point = pointsCopy.remove(0);
            Set<T> cluster = walkNearbyPoints(pointsCopy, limit, point);
            cluster.add(point);
            clusters.add(cluster);
        }
        return clusters;
    }

    private static <T extends MeasurableDistance> Set<T> walkNearbyPoints(List<T> points, double limit, T point) {
        Set<T> nearbyPoints = new HashSet<>();
        for (ListIterator<T> it = points.listIterator(); it.hasNext(); ) {
            T nearbyPoint = it.next();
            if (point.distanceTo(nearbyPoint) <= limit) {
                it.remove();
                nearbyPoints.add(nearbyPoint);
            }
        }
        if (!nearbyPoints.isEmpty()) {
            for (ListIterator<T> it = new ArrayList<>(nearbyPoints).listIterator(); it.hasNext() && !points.isEmpty(); ) {
                nearbyPoints.addAll(walkNearbyPoints(points, limit, it.next()));
            }
        }

        return nearbyPoints;
    }
}
