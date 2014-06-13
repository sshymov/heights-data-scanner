package org.ems.gui.components.palette;

import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * User: stas
 * Date: Sep 30, 2008
 */
public class GrayScalePalette extends AbstractPalette{
    public int getPaletteBits() {
        return 8;
    }

    protected PaletteData create() {
        List<RGB> colors=new ArrayList<RGB>();
        RGB white=new RGB(0xFF,0xFF,0xFF);
        RGB darkGray=new RGB(0x10,0x10,0x10);
        RGB black=new RGB(0x00,0x00,0x00);

        colors.add(black);
        colors.addAll(Arrays.asList(calculateMediateColors(darkGray,black,3)));
        colors.add(white);
        colors.addAll(Arrays.asList(calculateMediateColors(white,darkGray,250)));
        colors.add(darkGray);

        RGB[] palette=new RGB[getPaletteLength()];
        System.arraycopy(colors.toArray(),0,palette,0,getPaletteLength());

        return new PaletteData(palette);
    }

    public int getWaterIndex() {
        return 1;
    }

    public int getVoidValueIndex() {
        return 127;
    }

    public int getFlatLandBeginIndex() {
        return 128;
    }

    public int getFlatLandEndIndex() {
        return 255;
    }

    public int getMountainBeginIndex() {
        return 0;
    }

    public int getMountainEndIndex() {
        return 0;
    }
}
