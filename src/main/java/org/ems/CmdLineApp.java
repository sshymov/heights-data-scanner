package org.ems;

import java.io.IOException;
import java.util.Map;

import com.google.common.base.Function;
import org.apache.commons.compress.compressors.CompressorException;
import org.ems.model.*;
import org.ems.model.hgt.HGT;
import org.ems.scanners.ThresholdScanner;
import org.ems.service.DataStorage;
import org.ems.visualize.KmlBuilder;
import org.ems.visualize.OutputFormatBuilder;
import org.ems.visualize.PngBuilder;
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
    @Option(name = "-format", required = true, usage = "Output format")
    private OutputFormat format = OutputFormat.KML;
    @Option(name = "-min-steepness", usage = "Minimal steepness of hill in meters")
    private int minSteepness = 20;
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

        OutputFormatBuilder outputFormatBuilder;
        if (app.format == OutputFormat.KML) {
            outputFormatBuilder = new KmlBuilder(String.format("Scan for minSteepness=%d and minHeight=%d", app.minSteepness, app.minHeight), app.outputFileName);
        } else {
            outputFormatBuilder = new PngBuilder(app.outputFileName);
        }

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

        ThresholdScanner scanner = new ThresholdScanner(); //meters
        Function<MatrixCoordinate, ?> converter = getMatrixCoordinateFunction(app, hgt);
        outputFormatBuilder.startCoordinate(hgt, converter);

        System.out.print("Scanning...");
        for (Direction direction : Direction.values()) {
            Statistics stat = scanner.diffForDirection(direction, hgt);
            Map<MatrixCoordinate, Integer> scanResults = scanner.scanNotFixed(app.minSteepness, app.minHeight, direction);
            outputFormatBuilder.addDirection(direction, scanResults);
        }
        outputFormatBuilder.endCoordinate();

        System.out.println("complete");

    }

    private static Function<MatrixCoordinate, ?> getMatrixCoordinateFunction(CmdLineApp app, final HGT hgt) {
        Function<MatrixCoordinate, ?> converter;
        if (app.format == OutputFormat.KML) {
            converter = new Function<MatrixCoordinate, GeoCoordinate>() {

                @Override
                public GeoCoordinate apply(MatrixCoordinate matrixCoordinate) {
                    return hgt.calcCoordinateForCell(matrixCoordinate.x, matrixCoordinate.y);
                }
            };

        } else {
            converter = new Function<MatrixCoordinate, MatrixCoordinate>() {

                @Override
                public MatrixCoordinate apply(MatrixCoordinate matrixCoordinate) {
                    return matrixCoordinate;
                }
            };
        }
        return converter;
    }


}
