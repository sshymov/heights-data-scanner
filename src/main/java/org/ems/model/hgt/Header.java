package org.ems.model.hgt;

import org.ems.model.GeoCoordinate;
import org.ems.model.Latitude;
import org.ems.model.Longitude;

import java.text.ParseException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Created by IntelliJ IDEA.
 * User: Stas Shimov <stas.shimov@gmail.com>
 * Date: Oct 15, 2007
 * Time: 12:49:25 AM
 */
public class Header {
    private int width;
    private int height;
    private GeoCoordinate coordinate; // left bottom point of matrix
    private static final String FILENAME_FORMAT = ".*([NS])(\\d\\d)([EW])(\\d\\d\\d).*";


    public Header() {
    }

    public Header(long size, GeoCoordinate coordinate) {
        initDimension(size);
        this.coordinate = coordinate;
    }

    public Header(String filename, long size) throws ParseException, IllegalArgumentException {
        if (filename == null)
            throw new IllegalArgumentException("File name and size can not be null");
        initDimension(size);

        Pattern fileRE = Pattern.compile(FILENAME_FORMAT, Pattern.CASE_INSENSITIVE);
        Matcher matcher = fileRE.matcher(filename);
        if (!matcher.matches())
            throw new IllegalArgumentException("File name has wrong format");

        Latitude lat = new Latitude((matcher.group(1).equalsIgnoreCase("N") ? 1 : -1) * Double.parseDouble(matcher.group(2)));
        Longitude lon = new Longitude((matcher.group(3).equalsIgnoreCase("E") ? 1.0 : -1.0) * Double.parseDouble(matcher.group(4)));
        coordinate = new GeoCoordinate(lon, lat);
    }

    private void initDimension(long size) {
        long aspect = Math.round(Math.sqrt(size / 2));
        if (size % 2 > 0 || size/2!=Math.pow(aspect,2))
            throw new IllegalArgumentException("File size is wrong");
        width = height = (int) aspect;
    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public GeoCoordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(GeoCoordinate coordinate) {
        this.coordinate = coordinate;
    }
}
