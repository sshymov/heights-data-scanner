package org.ems.visualize;

import com.google.common.base.Function;
import org.ems.model.GeoCoordinate;
import org.ems.model.Direction;
import org.ems.model.MatrixCoordinate;
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
            stringBuilder.append("    <Style id=\"downArrowIcon" + direction.name() + "\">\n" +
                    "      <IconStyle>\n" +
                    "        <color>" + direction.getRgbColor().toKml() + "</color>\n" +
                    "        <Icon>\n" +
                    "          <href>http://maps.google.com/mapfiles/kml/pal4/icon28.png</href>\n" +
                    "        </Icon>\n" +
                    "      </IconStyle>\n" +
                    "    </Style>");
        }
    }

    @Override
    public void addDirection(Direction direction, Map<MatrixCoordinate, Integer> coordinates) {
        if (coordinates.isEmpty()) {
            return;
        }
        stringBuilder.append("<Folder>\n" +
                "<name>" + direction.getTitle() + "</name>\n" +
                "<visibility>0</visibility>");
        for (Map.Entry<MatrixCoordinate, Integer> entry : coordinates.entrySet()) {
            stringBuilder.append("\n      <Placemark>\n" +
                    "        <name>" + direction.getTitle() + " " + entry.getValue() + "m</name>\n" +
                    "        <visibility>1</visibility>\n" +
                    "        <styleUrl>#downArrowIcon" + direction.name() + "</styleUrl>\n" +
                    "        <Point>\n" +
                    "          <extrude>1</extrude>\n" +
                    "          <altitudeMode>relativeToGround</altitudeMode>\n" +
                    "          <coordinates>" + converter.apply(entry.getKey()).toDigitsString() + "," + entry.getValue() + "</coordinates>\n" +
                    "        </Point>\n" +
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
