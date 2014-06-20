package org.ems;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.ems.model.Coordinate;
import org.ems.model.Statistics;
import org.ems.model.hgt.HGT;
import org.ems.scanners.Direction;
import org.ems.scanners.ThresholdScanner;
import org.ems.service.DataStorage;
import org.ems.visualize.KmlBuilder;
import org.ems.visualize.PngUtils;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import sun.misc.IOUtils;

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

    @Option(name = "-lat", required = true, usage = "Latitude value, e.g.: 47")
    private int latitude;
    @Option(name = "-lon", required = true, usage = "Longitude value, e.g.: 30")
    private int longitude;
    @Option(name = "-format", required = true, usage = "Output format")
    private OutputFormat format = OutputFormat.KML;

    public static void main(String[] args) throws Exception {
        CmdLineApp app = new CmdLineApp();
        CmdLineParser cmdLineParser = new CmdLineParser(app);
        try {

            cmdLineParser.parseArgument(args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("java " + app.getClass().getSimpleName() + " [options...] ");
            cmdLineParser.printUsage(System.err);
            System.err.println();
            System.exit(1);
        }


        HGT hgt = new DataStorage().get(new Coordinate(app.longitude, app.latitude));
        if (hgt == null) return;

        ThresholdScanner scanner = new ThresholdScanner(hgt); //meters
        KmlBuilder kml = new KmlBuilder();
        for (Direction direction : Direction.values()) {
            Statistics stat = scanner.diffForDirection(direction);
            kml.addDirection(direction, scanner.scan(3, 70, direction));
        }

        System.out.println(kml.build());
        PngUtils.createPng(hgt.getHeightsMatrix(), new File("output.png"), stat.getMinHeight(), stat.getMaxHeight());


    }



}
