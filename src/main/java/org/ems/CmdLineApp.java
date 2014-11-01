package org.ems;

import java.io.IOException;
import java.util.Collection;

import com.google.common.base.Function;
import org.apache.commons.compress.compressors.CompressorException;
import org.ems.model.*;
import org.ems.model.hgt.HGT;
import org.ems.scanners.ThresholdScanner;
import org.ems.service.ClusterService;
import org.ems.service.DataStorage;
import org.ems.visualize.KmlBuilder;
import org.ems.visualize.OutputFormatBuilder;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Created by IntelliJ IDEA.
 * User: Stas Shimov <stas.shimov@gmail.com>
 * Date: Oct 15, 2007
 * Time: 12:47:01 AM
 */
public class CmdLineApp {


    enum OutputFormat {
        KML, PNG
    }

    @Option(name = "-lat", required = true, usage = "Latitude value, e.g.: 47 or 45-47")
    private String latitude;
    @Option(name = "-lon", required = true, usage = "Longitude value, e.g.: 30 or 29-33")
    private String longitude;
    @Option(name = "-min-avr-steepness", usage = "Minimal average slope in degrees from horizontal, e.g. 20")
    private int minAvrSlope;
    @Option(name = "-min-max-steepness", required = false, usage = "[optional] Minimal maximal slope of a segment in degrees from horizontal, e.g. 20")
    private Integer minMaxSlope;
    @Option(name = "-min-height", usage = "Minimal height of the hill in meters")
    private int minHeight = 70;
    @Argument(required = true)
    private String outputFileName;

    public static void main(String[] args) throws Exception {
        CmdLineApp app = new CmdLineApp();
        CmdLineParser cmdLineParser = new CmdLineParser(app);
        try {

            cmdLineParser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java " + app.getClass().getSimpleName() + " [options...] <outputfile>");
            cmdLineParser.printUsage(System.err);
            System.err.println();
            System.exit(1);
        }

        OutputFormatBuilder outputFormatBuilder = new KmlBuilder(String.format("Scan for minAvrSlope=%d , minMaxSlope=%d, minHeight=%d",
                app.minAvrSlope, app.minMaxSlope, app.minHeight), app.outputFileName);

        Range<Integer> latRange = parseRange(app.latitude);
        Range<Integer> lonRange = parseRange(app.longitude);
        for (int longitude = lonRange.getFrom(); longitude <= lonRange.getTo(); longitude++) {
            for (int latitude = latRange.getFrom(); latitude <= latRange.getTo(); latitude++) {
                processCoordinate(new GeoCoordinate(longitude, latitude), app, outputFormatBuilder);
            }
        }
        outputFormatBuilder.build();
        System.out.println("Done");
    }

    private static Range<Integer> parseRange(String longitude) {
        if (longitude == null || longitude.trim().isEmpty()) {
            throw new IllegalArgumentException("coordinate must not be empty");
        }
        String[] split = longitude.split("-");
        if (split.length > 2) {
            throw new IllegalArgumentException("coordinate has wrong format");
        }

        final int from = Integer.parseInt(split[0].trim());
        final int to = (split.length == 2) ? Integer.parseInt(split[1].trim()) : from;
        return new Range<>(from, to);
    }

    private static void processCoordinate(GeoCoordinate coordinate, CmdLineApp app, OutputFormatBuilder outputFormatBuilder) throws IOException, CompressorException {
        System.out.println("Getting data for " + coordinate + "...");
        final HGT hgt;
        try {
            hgt = new DataStorage().get(coordinate);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.err.println("Skipping " + coordinate);
            return;
        }

        Function<MatrixCoordinate, ?> converter = getMatrixCoordinateFunction(app, hgt);
        outputFormatBuilder.startCoordinate(hgt, converter);

        System.out.print("Scanning...");
        ClusterService clusterService=new ClusterService();
        for (Direction direction : Direction.values()) {
            ThresholdScanner scanner = ThresholdScanner.createScanner(direction, hgt);
            Collection<SlopeInfo> scanResults = scanner.scan(app.minAvrSlope, app.minHeight, app.minMaxSlope);
            clusterService.addDirectionPoints(scanResults);
        }
        for (Cluster cluster : clusterService.buildClusters()) {
            outputFormatBuilder.addCluster(cluster);
        }
        outputFormatBuilder.endCoordinate();

        System.out.println("complete");

    }

    private static Function<MatrixCoordinate, ?> getMatrixCoordinateFunction(CmdLineApp app, final HGT hgt) {
        Function<MatrixCoordinate, ?> converter;
            converter = new Function<MatrixCoordinate, GeoCoordinate>() {

                @Override
                public GeoCoordinate apply(MatrixCoordinate matrixCoordinate) {
                    return hgt.calcCoordinateForCell(matrixCoordinate.x, matrixCoordinate.y);
                }
            };

        return converter;
    }


}
