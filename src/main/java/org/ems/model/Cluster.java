package org.ems.model;

import java.util.*;

/**
 * Created by stas on 10/5/14.
 */
public class Cluster {
    private Map<Direction, List<SlopeInfo>> directionCoordinates = new HashMap<>();
    private Set<MatrixCoordinate> coordinates = new HashSet<>();
    private int size;

    public Map<Direction, List<SlopeInfo>> getDirectionCoordinates() {
        return directionCoordinates;
    }

    public int getSize() {
        return coordinates.size();
    }

    public void add(SlopeInfo slopeInfo) {
        if (!directionCoordinates.containsKey(slopeInfo.getDirection())) {
            directionCoordinates.put(slopeInfo.getDirection(), new ArrayList<SlopeInfo>());
        }
        List<SlopeInfo> matrixCoordinateSlopeInfoMap = directionCoordinates.get(slopeInfo.getDirection());
        matrixCoordinateSlopeInfoMap.add(slopeInfo);
        coordinates.add(slopeInfo.getHighPoint());
    }
}
