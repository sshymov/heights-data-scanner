package org.ems.clusterization;

/**
 * Created by stas on 10/28/14.
 */
public interface MeasurableDistance<T> {
    double distanceTo(T other);
}
