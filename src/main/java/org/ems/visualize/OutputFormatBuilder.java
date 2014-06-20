package org.ems.visualize;

import org.ems.model.Coordinate;
import org.ems.scanners.Direction;

import java.util.Map;

/**
 * Created by stas on 6/20/14.
 */
public interface OutputFormatBuilder {
    KmlBuilder addDirection(Direction direction, Map<Coordinate, Integer> coordinates);

    String build();
}
