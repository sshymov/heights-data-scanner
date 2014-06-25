package org.ems;

import java.util.Map;

import com.google.common.base.Function;
import org.ems.model.GeoCoordinate;
import org.ems.model.MatrixCoordinate;
import org.ems.model.Statistics;
import org.ems.model.hgt.HGT;
import org.ems.model.Direction;
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
    private int latitude;
    @Option(name = "-lon", required = true, usage = "Longitude value, e.g.: 30 or 29-33")
    private int longitude;
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


        System.out.println("Getting data...");
        final HGT hgt = new DataStorage().get(new GeoCoordinate(app.longitude, app.latitude));
        if (hgt == null) return;

        ThresholdScanner scanner = new ThresholdScanner(); //meters

        OutputFormatBuilder outputFormatBuilder;
        Function<MatrixCoordinate, ?> converter;
        if (app.format == OutputFormat.KML) {
            outputFormatBuilder = new KmlBuilder("Heights for "+hgt.getHeader().getCoordinate().toString());
            converter = new Function<MatrixCoordinate, GeoCoordinate>() {

                @Override
                public GeoCoordinate apply(MatrixCoordinate matrixCoordinate) {
                    return hgt.calcCoordinateForCell(matrixCoordinate.x, matrixCoordinate.y);
                }
            };
        } else {
            outputFormatBuilder = new PngBuilder(hgt.getHeightsMatrix());
            converter = new Function<MatrixCoordinate, MatrixCoordinate>() {

                @Override
                public MatrixCoordinate apply(MatrixCoordinate matrixCoordinate) {
                    return matrixCoordinate;
                }
            };
        }

        System.out.println("Scanning...");
        for (Direction direction : Direction.values()) {
            Statistics stat = scanner.diffForDirection(direction, hgt);
            Map<?, Integer> scanResults = scanner.scanNotFixed(app.minSteepness, app.minHeight, direction, converter);
            outputFormatBuilder.addDirection(direction, scanResults);
        }
        System.out.println("Writing...");

        outputFormatBuilder.build(app.outputFileName);
        System.out.println("Done");
    }


}
