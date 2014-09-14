package org.ems.visualize;

import com.google.common.base.Function;
import org.ems.model.GeoCoordinate;
import org.ems.model.Direction;
import org.ems.model.MatrixCoordinate;
import org.ems.model.SlopeInfo;
import org.ems.model.hgt.HGT;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public void addDirection(Direction direction, Map<MatrixCoordinate, SlopeInfo> coordinates) {
        if (coordinates.isEmpty()) {
            return;
        }
        stringBuilder.append("<Folder>\n" +
                "<name>" + direction.getTitle() + "</name>\n" +
                "<visibility>0</visibility>");
        for (Map.Entry<MatrixCoordinate, SlopeInfo> entry : coordinates.entrySet()) {
            stringBuilder.append("\n      <Placemark>\n" +
                    "        <name>" + direction.name() + ", " + entry.getValue().getElevationGain()
                    + "m, Avr=" + entry.getValue().getAvg()
                    + "°, Max=" + entry.getValue().getMax() + "°</name>\n" +
                    "        <visibility>1</visibility>\n" +
                    "        <description>" + direction.getTitle() + ", Elevation Gain: " + entry.getValue().getElevationGain() +
                    "m,\n Average Slope: " + entry.getValue().getAvg() +
                    "°,\n Maximum Slope: " + entry.getValue().getMax() + "°</description>" +
                    "        <styleUrl>#line" + direction.name() + "</styleUrl>\n" +
                    "        <LineString>\n" +
                    "          <extrude>1</extrude>\n" +
                    "          <tessellate>1</tessellate>\n" +
                    "          <altitudeMode>relativeToGround</altitudeMode>\n" +
                    "          <coordinates> " + converter.apply(entry.getValue().getFirstPoint()).toDigitsString() + "," + entry.getValue().getElevationGain() + "\n" +
                    "            " + converter.apply(entry.getKey()).toDigitsString() + ",0 </coordinates>\n" +
                    "        </LineString>\n"+
                    "      </Placemark>");
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
