package org.ems.visualize;

import com.google.common.base.Function;
import org.ems.model.*;
import org.ems.model.hgt.HGT;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Created by stas on 6/11/14.
 */
public class KmlBuilder implements OutputFormatBuilder<GeoCoordinate> {
    private final String outputName;
    private StringBuilder stringBuilder = new StringBuilder();
    private Function<MatrixCoordinate, GeoCoordinate> converter;

    public KmlBuilder(String title, String outputName) {
        this.outputName = outputName;

        stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
                "  <Document>\n" +
                "    <name>" + title + "</name>\n" +
                "    <open>1</open>\n" +
                "      \n"
        );
        for (Direction direction : Direction.values()) {
            stringBuilder.append("    <Style id=\"line" + direction.name() + "\">\n" +
                    "      <LineStyle>\n" +
                    "        <color>" + direction.getRgbColor().toKml() + "</color>\n" +
                    "        <width>4</width>\n" +
                    "      </LineStyle>\n" +
                    "      <PolyStyle>\n" +
                    "        <color>7f7f7f7f</color>\n" +
                    "      </PolyStyle>\n" +
                    "    </Style>\n");
        }
    }

    @Override
    public void addCluster(Cluster cluster) {

        Map<Direction, List<SlopeInfo>> directionsMap = cluster.getDirectionCoordinates();
        stringBuilder.append("<Folder>\n" +
                "<name>");
        for (Map.Entry<Direction, List<SlopeInfo>> entry : directionsMap.entrySet()) {
            stringBuilder.append(entry.getKey().name() + "(" + entry.getValue().size() + ")");
        }
        stringBuilder.append("</name>\n" +
                "<visibility>1</visibility>");
        for (Map.Entry<Direction, List<SlopeInfo>> dirCoordinates : directionsMap.entrySet()) {
            Direction direction = dirCoordinates.getKey();
            stringBuilder.append("<Folder>\n" +
                    "<name>" + direction.getTitle() + "</name>\n" +
                    "<visibility>0</visibility>");
            for (SlopeInfo entry : dirCoordinates.getValue()) {
                stringBuilder.append("\n      <Placemark>\n" +
                        "        <name>" + direction.name() + ", " + entry.getElevationGain()
                        + "m, Avr=" + entry.getAvg()
                        + "°, Max=" + entry.getMax() + "°</name>\n" +
                        "        <visibility>1</visibility>\n" +
                        "        <description>" + direction.getTitle() + ", Elevation Gain: " + entry.getElevationGain() +
                        "m,\n Average Slope: " + entry.getAvg() +
                        "°,\n Maximum Slope: " + entry.getMax() + "°</description>" +
                        "        <styleUrl>#line" + direction.name() + "</styleUrl>\n" +
                        "        <LineString>\n" +
                        "          <extrude>1</extrude>\n" +
                        "          <tessellate>1</tessellate>\n" +
                        "          <altitudeMode>relativeToGround</altitudeMode>\n" +
                        "          <coordinates> " + converter.apply(entry.getLowPoint()).toDigitsString() + "," + entry.getElevationGain() + "\n" +
                        "            " + converter.apply(entry.getHighPoint()).toDigitsString() + ",0 </coordinates>\n" +
                        "        </LineString>\n" +
                        "      </Placemark>");
            }
            stringBuilder.append("</Folder>");
        }
        stringBuilder.append("</Folder>");
    }

    @Override
    public void build() throws IOException {
        stringBuilder.append("  </Document>\n" +
                "</kml>");
        Files.write(Paths.get(outputName.endsWith(".kml") ? outputName : outputName + ".kml"),
                stringBuilder.toString().getBytes());
    }

    @Override
    public void startCoordinate(HGT coordinate, Function<MatrixCoordinate, GeoCoordinate> converter) {
        this.converter = converter;
        stringBuilder.append("<Folder>\n" +
                "<name>" + coordinate.getHeader().getCoordinate() + "</name>\n" +
                "<visibility>1</visibility>");

    }

    @Override
    public void endCoordinate() {
        stringBuilder.append("</Folder>");

    }
}
