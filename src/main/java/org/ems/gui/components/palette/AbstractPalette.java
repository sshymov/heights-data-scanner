package org.ems.gui.components.palette;

import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

/**
 * User: stas
 * Date: Sep 30, 2008
 */
public abstract class AbstractPalette {
    protected PaletteData paletteData;

    public abstract int getPaletteBits();

    public int getPaletteLength() {
        return (int)Math.round(Math.pow(2.0,(double)getPaletteBits()));
    }

    public PaletteData getPaletteData() {
        return paletteData!=null?paletteData:(paletteData=create());
    }

    protected abstract PaletteData create() ;

    protected RGB[] calculateMediateColors(RGB from, RGB to , int numberOfColors) {
        RGB[] colors=new RGB[numberOfColors];
        float redStep=((float)(to.red-from.red))/(numberOfColors+1);
        float greenStep=((float)(to.green-from.green))/(numberOfColors+1);
        float blueStep=((float)(to.blue-from.blue))/(numberOfColors+1);
        for (int i=1;i<=numberOfColors;i++) {
            colors[i-1]=new RGB(from.red+Math.round(redStep*i),from.green+Math.round(greenStep*i),from.blue+Math.round(blueStep*i));
        }
        return colors;
    }

    /**
     * First not water value resides by this index
     * @return
     */
    public abstract int getWaterIndex();
    public abstract int getFlatLandBeginIndex();
    public abstract int getFlatLandEndIndex();
    public abstract int getMountainBeginIndex();
    public abstract int getMountainEndIndex();
    public abstract int getVoidValueIndex();

}
