package org.ems.visualize;

import org.ems.scanners.Direction;

import java.io.IOException;
import java.util.Map;

/**
 * Created by stas on 6/20/14.
 */
public interface OutputFormatBuilder<T> {
    void addDirection(Direction direction, Map<T, Integer> coordinates);

    void build(String outputFileName) throws IOException;
}
