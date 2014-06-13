package org.ems.visualize;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;
import ar.com.hjg.pngj.chunks.PngChunkTextVar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by stas on 6/9/14.
 */
public class PngUtils {

    public static void createPng(int[][] matrix, File outputPng, int min, int max) {

        try (FileOutputStream fos = new FileOutputStream(outputPng)) {
            ImageInfo imi = new ImageInfo(matrix[0].length, matrix.length, 8, false); // 8 bits per channel, no alpha
            // open image for writing to a output stream
            PngWriter png = new PngWriter(fos, imi);
            // add some optional metadata (chunks)
            png.getMetadata().setDpi(100.0);
            png.getMetadata().setTimeNow(0); // 0 seconds fron now = now
            for (int row = 0; row < png.imgInfo.rows; row++) {
                ImageLineInt iline = new ImageLineInt(imi);
                for (int col = 0; col < imi.cols; col++) {
                    if (matrix[row][col] > 0) {
                        int r = 255-matrix[row][col];
                        int g = 255-matrix[row][col];
                        int b = 255-matrix[row][col];
                        ImageLineHelper.setPixelRGB8(iline, col, r, g, b); // orange-ish gradient
                    } else {
                        int r = 0;
                        int g = 0;
                        int b = 128-matrix[row][col];
                        ImageLineHelper.setPixelRGB8(iline, col, r, g, b); // orange-ish gradient
                    }

                }
                png.writeRow(iline);
            }
            png.end();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
