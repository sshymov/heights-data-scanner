package org.ems.model.hgt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.annotations.VisibleForTesting;
import org.ems.model.GeoCoordinate;

/**
 * User: Stas Shimov <stas.shimov@gmail.com>
 * Date: Sep 27, 2008
 * Time: 3:36:49 PM
 */
public class HGT {
    private int[][] heightsMatrix;
    private Header header;

    public static HGT create(String file) throws Exception {

        File source = new File(file);
        Header header = new Header(file, source.length());
        HGT hgt = new HGT(header);
        try (FileInputStream fis = new FileInputStream(source)) {
            hgt.fill(fis);
        }
        return hgt;
    }

    public static HGT create(GeoCoordinate coordinate, long size, InputStream inputStream) throws IOException {
        Header header = new Header(size, coordinate);
        HGT hgt = new HGT(header);
        hgt.fill(inputStream);
        return hgt;
    }

    @VisibleForTesting
    public static HGT create(GeoCoordinate coordinate, int[][] heightsMatrix) throws IOException {
        HGT hgt = new HGT();
        hgt.header = new Header(2*heightsMatrix.length*heightsMatrix.length, coordinate);
        hgt.heightsMatrix=heightsMatrix;
        return hgt;
    }

    private HGT() {
    }

    protected HGT(Header header) {
        this.header = header;
        heightsMatrix = new int[header.getHeight()][header.getWidth()]; //1201x1201
    }

    protected void fill(InputStream istream) throws IOException {
        byte[] rowBuf = new byte[header.getWidth() * 2];
        for (int row = 0; row < header.getHeight(); row++) {

            int len =0 ;
            int offset=0;
            do {
            len= istream.read(rowBuf, offset, header.getWidth() * 2-offset);
                offset+=len;
            } while(len>0) ;

            if (offset != header.getWidth() * 2)
                throw new IOException("Unexpected end of HGT file");

            for (int col = 0; col < header.getWidth(); col++) {
                heightsMatrix[row][col] = (rowBuf[col * 2] << 8) | (0x000000FF & (int) rowBuf[col * 2 + 1]);
            }
        }

    }

    public GeoCoordinate calcCoordinateForCell(int x, int y) {
        return getHeader().getCoordinate().shift(1.0 * x / (header.getWidth()-1),
                1.0 - 1.0 * y / (header.getHeight()-1));
    }

    public int[][] getHeightsMatrix() {
        return heightsMatrix;
    }

    public Header getHeader() {
        return header;
    }
}
