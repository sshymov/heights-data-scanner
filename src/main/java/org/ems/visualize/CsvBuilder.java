package org.ems.visualize;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.commons.compress.utils.IOUtils;
import org.ems.model.*;
import org.ems.model.hgt.HGT;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by stas on 11/4/14.
 */
public class CsvBuilder implements OutputFormatBuilder<MatrixCoordinate> {
    private CSVWriter writer;
    private HGT hgt;
    private long clusterCounter;

    public CsvBuilder(String outputName) throws IOException {
        writer = new CSVWriter(new FileWriter(outputName.endsWith(".csv") ? outputName : outputName + ".csv"));
        writer.writeNext(new String[] {"direction", "high_point", "low_point", "cluster_id", "avr_slope", "max_slope", "elevation_gain"});
    }

    @Override
    public void build() throws IOException {
        IOUtils.closeQuietly(writer);
    }

    @Override
    public void startCoordinate(HGT coordinate) throws IOException {
        hgt=coordinate;

    }

    @Override
    public void endCoordinate() throws IOException {
        hgt=null;

    }

    @Override
    public void addCluster(Cluster cluster) throws IOException {
        clusterCounter++;
        for (Map.Entry<Direction, List<SlopeInfo>> entry: cluster.getDirectionCoordinates().entrySet()) {
            for (SlopeInfo slopeInfo: entry.getValue()) {
                writer.writeNext(new String[] {slopeInfo.getDirection().name(),  toGeo(slopeInfo.getHighPoint()).toString(),
                        toGeo(slopeInfo.getLowPoint()).toString(),String.valueOf(clusterCounter),
                        String.valueOf(slopeInfo.getAvg()), String.valueOf(slopeInfo.getMax()),
                        String.valueOf(slopeInfo.getElevationGain())});
            }
        }

    }

    private GeoCoordinate toGeo(MatrixCoordinate matrixCoordinate) {
        return hgt.calcCoordinateForCell(matrixCoordinate.x, matrixCoordinate.y);
    }
}
