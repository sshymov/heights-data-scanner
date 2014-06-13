package org.ems.gui.components.palette;

import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: stas
 * Date: Sep 30, 2008
 */
public class PhysicalMapPolette extends AbstractPalette {
    public int getPaletteBits() {
        return 8;
    }

    protected PaletteData create() {
        List<RGB> colors=new ArrayList<RGB>();
        RGB deepDarkBlue=new RGB(0x2b,0x64,0xa0);
        RGB darkBlue=new RGB(0x9a,0xd9,0xea);
        RGB blue=new RGB(0xb3,0xe1,0xee);
        RGB lightBlue=new RGB(0xe6,0xf5,0xfa);
        RGB darkGreen=new RGB(0xa7,0xd8,0x49);
        RGB lightGreen=new RGB(0xd9,0xf0,0x98);
        RGB yellow=new RGB(0xff,0xf2,0xbb);
        RGB lightBrown=new RGB(0xf0,0xb7,0x6a);
        RGB brown=new RGB(0xa9,0x58,0x2c);
        RGB darkBrown=new RGB(0x55,0x24,0x09);
        RGB black=new RGB(0x00,0x00,0x00);

        colors.add(deepDarkBlue);
//        colors.addAll(Arrays.asList(calculateMediateColors(deepDarkBlue,darkBlue,10)));
//        colors.add(darkBlue);
//        colors.addAll(Arrays.asList(calculateMediateColors(darkBlue,blue,10)));
//        colors.add(blue);
//        colors.addAll(Arrays.asList(calculateMediateColors(blue,lightBlue,10)));
//        colors.add(lightBlue);
        colors.add(darkGreen);
        colors.addAll(Arrays.asList(calculateMediateColors(darkGreen,lightGreen,50)));
        colors.add(lightGreen);
        colors.addAll(Arrays.asList(calculateMediateColors(lightGreen,yellow,50)));
        colors.add(yellow);
        colors.addAll(Arrays.asList(calculateMediateColors(yellow,lightBrown,50)));
        colors.add(lightBrown);
        colors.addAll(Arrays.asList(calculateMediateColors(lightBrown,brown,50)));
        colors.add(brown);
        colors.addAll(Arrays.asList(calculateMediateColors(brown,darkBrown,48)));
        colors.add(darkBrown);
        colors.add(black);

        RGB[] palette=new RGB[getPaletteLength()];
        System.arraycopy(colors.toArray(),0,palette,0,getPaletteLength());

        return new PaletteData(palette);
    }

    public int getWaterIndex() {
        return 0;
    }

    public int getFlatLandBeginIndex() {
        return 1;
    }

    public int getFlatLandEndIndex() {
        return 154;
    }

    public int getMountainBeginIndex() {
        return 155;
    }

    public int getMountainEndIndex() {
        return 254;
    }

    public int getVoidValueIndex() {
        return 255;
    }
}
