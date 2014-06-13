package org.ems.visualize;

import org.ems.model.Coordinate;
import org.ems.scanners.Direction;

import java.util.List;
import java.util.Map;

/**
 * Created by stas on 6/11/14.
 */
public class KmlBuilder {
    private StringBuilder stringBuilder=new StringBuilder();
    public KmlBuilder() {
        stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
                "  <Document>\n" +
                "    <name>Heights</name>\n" +
                "    <open>1</open>\n" +
                "      \n" +
                "    <Style id=\"downArrowIcon\">\n" +
                "      <IconStyle>\n" +
                "        <Icon>\n" +
                "          <href>http://maps.google.com/mapfiles/kml/pal4/icon28.png</href>\n" +
                "        </Icon>\n" +
                "      </IconStyle>\n" +
                "    </Style>");
    }
    public KmlBuilder addDirection(Direction direction, Map<Coordinate, Integer> coordinates) {
        stringBuilder.append("<Folder>\n" +
                "<name>"+direction.getTitle()+"</name>\n" +
                "<visibility>0</visibility>");
        for (Map.Entry<Coordinate, Integer> entry : coordinates.entrySet()) {
            stringBuilder.append("\n      <Placemark>\n" +
                    "        <name>"+direction.getTitle()+" "+entry.getValue()+"m</name>\n" +
                    "        <visibility>0</visibility>\n" +
                    "        <styleUrl>#downArrowIcon</styleUrl>\n" +
                    "        <Point>\n" +
                    "          <extrude>1</extrude>\n" +
                    "          <altitudeMode>relativeToGround</altitudeMode>\n" +
                    "          <coordinates>"+entry.getKey().toDigitsString()+","+entry.getValue()+"</coordinates>\n" +
                    "        </Point>\n" +
                    "      </Placemark>");
        }
        stringBuilder.append("</Folder>");
        return this;
    }

    public String build() {
        stringBuilder.append("  </Document>\n" +
                "</kml>");
         return stringBuilder.toString();
    }
}
