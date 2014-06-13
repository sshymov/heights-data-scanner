package org.ems.model;

import org.ems.scanners.Direction;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: Stas Shimov <stas.shimov@gmail.com>
 * Date: Sep 27, 2008
 * Time: 4:04:28 PM
 */
public class DiffPoint extends Coordinate {
    
    private Map<Direction,Integer> directionsAltitudeMap=new LinkedHashMap<Direction,Integer>();

    public DiffPoint(Coordinate coordinate) {
        super(coordinate);
    }

    public Map<Direction, Integer> getDirectionsAltitudeMap() {
        return directionsAltitudeMap;
    }
}
