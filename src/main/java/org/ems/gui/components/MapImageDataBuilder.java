package org.ems.gui.components;

import org.eclipse.swt.graphics.ImageData;
import org.ems.model.hgt.HGT;
import org.ems.gui.components.palette.AbstractPalette;
import org.ems.scanners.IScanner;
import org.ems.scanners.ThresholdScanner;

/**
 * User: stas
 * Date: Sep 30, 2008
 */
public class MapImageDataBuilder {

    private ImageData mapData;
    private AbstractPalette palette;

    public MapImageDataBuilder(int width, int height, AbstractPalette palette) {
        create(width,height,palette);
    }

    private void create(int width, int height, AbstractPalette palette){
        mapData = new ImageData(width,height,
                palette.getPaletteBits(), palette.getPaletteData());
        this.palette=palette;
    }

    public void drawPhysicalMap(HGT hgt) {
        int[][] origMatrix=hgt.getHeightsMatrix();
        int[] minMax=getMinMaxValues(origMatrix);
        float koef;
        if (minMax[0]<0)
            minMax[0]=0;
        if (minMax[1]<0)
            minMax[1]=1;
        if ((minMax[1]-minMax[0]) > 500)        
                koef=((float)(palette.getMountainEndIndex()-palette.getFlatLandBeginIndex()+1))/(minMax[1]-minMax[0]+1);
        else
            koef=((float)(palette.getFlatLandEndIndex()-palette.getFlatLandBeginIndex()+1))/(minMax[1]-minMax[0]+1);
//        float subWaterColourKoef=((float)palette.getWaterEndIndex())/subWaterMin;
//        float overWaterColourKoef=((float)(palette.getPaletteLength()-palette.getWaterEndIndex()))/overWaterMax;
        int color;
        for (int r=0;r<origMatrix.length;r++)
            for (int c=0;c<origMatrix[r].length;c++) {
                int height=origMatrix[r][c];
                if (height== ThresholdScanner.VOID_VALUE)
                    mapData.setPixel(c,r,palette.getVoidValueIndex());
                else if (height>0) {
                    color=palette.getFlatLandBeginIndex()+Math.round(koef*(height-minMax[0]));
                    assert color<=palette.getMountainEndIndex();
                    mapData.setPixel(c,r,color);
                }
                else //<=0
                    mapData.setPixel(c,r,palette.getWaterIndex());
            }
    }
/*
    public void drawPhysicalMap(HGT hgt) {
        int[][] origMatrix=hgt.getHeightsMatrix();
        int[] minMax=getMinMaxValues(origMatrix);
        int subWaterMin=200;
        int overWaterMax=2000;
        float subWaterColourKoef=((float)palette.getWaterEndIndex())/subWaterMin;
        float overWaterColourKoef=((float)(palette.getPaletteLength()-palette.getWaterEndIndex()))/overWaterMax;
        for (int r=0;r<origMatrix.length;r++)
            for (int c=0;c<origMatrix[r].length;c++) {
                if (origMatrix[r][c]== IScanner.VOID_VALUE)
                    mapData.setPixel(c,r,palette.getVoidValueIndex());
                else if (origMatrix[r][c]<-subWaterMin)
                    mapData.setPixel(c,r,0);
                else if (origMatrix[r][c]>=overWaterMax)
                    mapData.setPixel(c,r,254);
                else if (origMatrix[r][c]>0)
                    mapData.setPixel(c,r,palette.getWaterEndIndex()+Math.round(overWaterColourKoef*origMatrix[r][c]));
                else //<=0
                    mapData.setPixel(c,r,Math.round(subWaterColourKoef*Math.abs(origMatrix[r][c])));
            }
    }
*/    
    private int[] getMinMaxValues(int[][] origMatrix) {
        int [] results=new int[2];
        results[0]=32768;//min
        results[1]=-32768;//max
                results[1]=0;
        for (int r=0;r<origMatrix.length;r++)
            for (int c=0;c<origMatrix[r].length;c++) {
                //min
                if (origMatrix[r][c]<results[0]
                        && origMatrix[r][c]!= ThresholdScanner.VOID_VALUE)
                    results[0]=origMatrix[r][c];
                //max
                if (origMatrix[r][c]>results[1])
                    results[1]=origMatrix[r][c];
            }

        return results;

    }

    public ImageData getMapData() {
        return mapData;
    }
}
