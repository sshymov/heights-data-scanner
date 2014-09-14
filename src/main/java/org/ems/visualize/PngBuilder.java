package org.ems.visualize;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;
import com.google.common.base.Function;
import org.ems.model.*;
import org.ems.model.hgt.HGT;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by stas on 6/20/14.
 */
public class PngBuilder implements OutputFormatBuilder<MatrixCoordinate> {
    private int[][] matrix;
    private Map<MatrixCoordinate, RGB> results = new HashMap<>();
    private Function<MatrixCoordinate, ?> converter;
    private String outputName;
    private GeoCoordinate geoCoordinate;

    public PngBuilder(String outputName) {
        this.outputName = outputName;
    }

    @Override
    public void addDirection(Direction direction, Map<MatrixCoordinate, SlopeInfo> coordinates) {
        for (MatrixCoordinate coordinate : coordinates.keySet()) {
            results.put(coordinate, direction.getRgbColor());
        }
    }


    @Override
    public void build() {
    }

    private void drawCross(ImageLineInt iline, int col, RGB rgb) {
        ImageLineHelper.setPixelRGB8(iline, col - 1, rgb.red, rgb.green, rgb.blue);
        ImageLineHelper.setPixelRGB8(iline, col, rgb.red, rgb.green, rgb.blue);
        ImageLineHelper.setPixelRGB8(iline, col + 1, rgb.red, rgb.green, rgb.blue);
    }

    @Override
    public void startCoordinate(HGT hgt, Function<MatrixCoordinate, MatrixCoordinate> converter) {
        matrix = hgt.getHeightsMatrix();
        geoCoordinate = hgt.getHeader().getCoordinate();
        this.converter = converter;
    }

    @Override
    public void endCoordinate() {
        try (FileOutputStream fos = new FileOutputStream(outputName + geoCoordinate + ".png")) {
            ImageInfo imi = new ImageInfo(matrix[0].length, matrix.length, 8, false); // 8 bits per channel, no alpha
            // open image for writing to a output stream
            PngWriter png = new PngWriter(fos, imi);
            // add some optional metadata (chunks)
            png.getMetadata().setDpi(100.0);
            png.getMetadata().setTimeNow(0); // 0 seconds fron now = now
            MatrixCoordinate matrixCoordinate = new MatrixCoordinate();
            for (int row = 0; row < imi.rows; row++) {
                ImageLineInt iline = new ImageLineInt(imi);
                for (int col = 0; col < imi.cols; col++) {
                    if (matrix[row][col] > 0) {
                        matrixCoordinate.setValues(col, row);
                        RGB rgb = results.get(matrixCoordinate);
                        if (rgb != null) {
                            drawCross(iline, col, rgb);
                        } else {
                            int r = 255 - matrix[row][col];
                            int g = 255 - matrix[row][col];
                            int b = 255 - matrix[row][col];
                            ImageLineHelper.setPixelRGB8(iline, col, r, g, b); // orange-ish gradient
                        }
                    } else {
                        int r = 0;
                        int g = 0;
                        int b = 128 - matrix[row][col];
                        ImageLineHelper.setPixelRGB8(iline, col, r, g, b); // orange-ish gradient
                    }
                }
                png.writeRow(iline);
            }


            png.end();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
