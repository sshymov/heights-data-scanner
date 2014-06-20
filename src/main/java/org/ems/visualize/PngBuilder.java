package org.ems.visualize;

import org.ems.model.Coordinate;
import org.ems.scanners.Direction;

import java.util.Map;

/**
 * Created by stas on 6/20/14.
 */
public class PngBuilder implements OutputFormatBuilder {
    @Override
    public KmlBuilder addDirection(Direction direction, Map<Coordinate, Integer> coordinates) {
        return null;
    }

    @Override
    public String build() {
        return null;
    }
}
