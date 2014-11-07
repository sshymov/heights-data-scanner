package org.ems.visualize;

import org.apache.commons.compress.utils.IOUtils;
import org.ems.model.*;
import org.ems.model.hgt.HGT;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * Created by stas on 6/11/14.
 */
public class KmlBuilder implements OutputFormatBuilder<GeoCoordinate> {
    private Writer writer;
    private HGT hgt;

    public KmlBuilder(String title, String outputName) throws IOException {
            writer = new BufferedWriter(new FileWriter(outputName.endsWith(".kml") ? outputName : outputName + ".kml"));


            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
                    "  <Document>\n" +
                    "    <name>" + title + "</name>\n" +
                    "    <open>1</open>\n" +
                    "      \n"
            );
            for (Direction direction : Direction.values()) {
                writer.append("    <Style id=\"line" + direction.name() + "\">\n" +
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
    public void addCluster(Cluster cluster) throws IOException {

        Map<Direction, List<SlopeInfo>> directionsMap = cluster.getDirectionCoordinates();
        writer.write("<Folder>\n" +
                "<name>");
        for (Map.Entry<Direction, List<SlopeInfo>> entry : directionsMap.entrySet()) {
            writer.write(entry.getKey().name() + "(" + entry.getValue().size() + ")");
        }
        writer.write("</name>\n" +
                "<visibility>1</visibility>");
        for (Map.Entry<Direction, List<SlopeInfo>> dirCoordinates : directionsMap.entrySet()) {
            Direction direction = dirCoordinates.getKey();
            writer.write("<Folder>\n" +
                    "<name>" + direction.getTitle() + "</name>\n" +
                    "<visibility>0</visibility>");
            for (SlopeInfo entry : dirCoordinates.getValue()) {
                writer.write("\n      <Placemark>\n" +
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
                        "          <coordinates> " + toGeo(entry.getLowPoint()).toDigitsString() + "," + entry.getElevationGain() + "\n" +
                        "            " + toGeo(entry.getHighPoint()).toDigitsString() + ",0 </coordinates>\n" +
                        "        </LineString>\n" +
                        "      </Placemark>");
            }
            writer.write("</Folder>");
        }
        writer.write("</Folder>");

    }

    private GeoCoordinate toGeo(MatrixCoordinate matrixCoordinate) {
        return hgt.calcCoordinateForCell(matrixCoordinate.x, matrixCoordinate.y);
    }

    @Override
    public void build() throws IOException {
        try {
            writer.append("  </Document>\n" +
                    "</kml>");
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    @Override
    public void startCoordinate(HGT coordinate) throws IOException {
        this.hgt = coordinate;
        writer.write("<Folder>\n" +
                "<name>" + coordinate.getHeader().getCoordinate() + "</name>\n" +
                "<visibility>0</visibility>");

    }

    @Override
    public void endCoordinate() throws IOException {
        writer.write("</Folder>");
    }
}
