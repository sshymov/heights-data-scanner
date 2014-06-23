package org.ems.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: Stas Shimov <stas.shimov@gmail.com>
 * Date: Sep 27, 2008
 * Time: 4:04:28 PM
 */
public class DiffPoint extends GeoCoordinate {
    
    private Map<Direction,Integer> directionsAltitudeMap=new LinkedHashMap<Direction,Integer>();

    public DiffPoint(GeoCoordinate coordinate) {
        super(coordinate);
    }

    public Map<Direction, Integer> getDirectionsAltitudeMap() {
        return directionsAltitudeMap;
    }
}
