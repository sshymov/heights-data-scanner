package org.ems.visualize;

import org.ems.model.GeoCoordinate;
import org.ems.scanners.Direction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Created by stas on 6/11/14.
 */
public class KmlBuilder implements OutputFormatBuilder<GeoCoordinate> {
    private StringBuilder stringBuilder=new StringBuilder();
    public KmlBuilder() {
        stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
                "  <Document>\n" +
                "    <name>Heights</name>\n" +
                "    <open>1</open>\n" +
                "      \n"
);
        for (Direction direction : Direction.values()) {
            stringBuilder.append(                "    <Style id=\"downArrowIcon"+direction.name()+"\">\n" +
                    "      <IconStyle>\n" +
                    "        <color>ff"+direction.getRgbColor()+"</color>\n" +
                    "        <Icon>\n" +
                    "          <href>http://maps.google.com/mapfiles/kml/pal4/icon28.png</href>\n" +
                    "        </Icon>\n" +
                    "      </IconStyle>\n" +
                    "    </Style>");
        }
    }
    @Override
    public void addDirection(Direction direction, Map<GeoCoordinate, Integer> coordinates) {
        stringBuilder.append("<Folder>\n" +
                "<name>"+direction.getTitle()+"</name>\n" +
                "<visibility>0</visibility>");
        for (Map.Entry<GeoCoordinate, Integer> entry : coordinates.entrySet()) {
            stringBuilder.append("\n      <Placemark>\n" +
                    "        <name>"+direction.getTitle()+" "+entry.getValue()+"m</name>\n" +
                    "        <visibility>1</visibility>\n" +
                    "        <styleUrl>#downArrowIcon"+direction.name()+"</styleUrl>\n" +
                    "        <Point>\n" +
                    "          <extrude>1</extrude>\n" +
                    "          <altitudeMode>relativeToGround</altitudeMode>\n" +
                    "          <coordinates>"+entry.getKey().toDigitsString()+","+entry.getValue()+"</coordinates>\n" +
                    "        </Point>\n" +
                    "      </Placemark>");
        }
        stringBuilder.append("</Folder>");
    }

    @Override
    public void build(String outputFileName) throws IOException {
        stringBuilder.append("  </Document>\n" +
                "</kml>");
         Files.write(Paths.get(outputFileName), stringBuilder.toString().getBytes());
    }
}
