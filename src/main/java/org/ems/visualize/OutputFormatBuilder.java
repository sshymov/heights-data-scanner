package org.ems.visualize;

import com.google.common.base.Function;
import org.ems.model.*;
import org.ems.model.hgt.HGT;

import java.io.IOException;
import java.util.Map;

/**
 * Created by stas on 6/20/14.
 */
public interface OutputFormatBuilder<T> {

    void build() throws IOException;

    void startCoordinate(HGT coordinate, Function<MatrixCoordinate, T> converter);

    void endCoordinate();

    void addCluster(Cluster cluster);
}
