package org.ems.visualize;

import com.google.common.base.Function;
import org.ems.model.Direction;
import org.ems.model.GeoCoordinate;
import org.ems.model.MatrixCoordinate;
import org.ems.model.hgt.HGT;

import java.io.IOException;
import java.util.Map;

/**
 * Created by stas on 6/20/14.
 */
public interface OutputFormatBuilder<T> {
    void addDirection(Direction direction, Map<T, Integer> coordinates);

    void build() throws IOException;

    void startCoordinate(HGT coordinate, Function<MatrixCoordinate, ?> converter);

    void endCoordinate();
}
