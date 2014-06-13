package org.ems;

import org.ems.gui.GeoAppWindow;
import org.eclipse.swt.widgets.Display;

/**
 * Created by IntelliJ IDEA.
 * User: Stas Shimov <stas.shimov@gmail.com>
 * Date: Oct 15, 2007
 * Time: 12:47:01 AM
 */
public class App {
    public static void main(String[] args) {
        GeoAppWindow win=new GeoAppWindow();
        win.setBlockOnOpen(true);
        win.open();
        Display.getCurrent().dispose();

        /*
        HGT hgt=HGT.create("res/hgt/N48E030.hgt");
        if (hgt==null) return;
        int [][] matrix=hgt.getHeightsMatrix();

        ThresholdScanner scanner=new ThresholdScanner(60); //meters
        Coordinate coordinate;
        Map<IScanner.Direction,Integer> result;
        for (int r=1;r<matrix.length-1;r++)
            for (int c=1;c<matrix[r].length-1;c++) {
                 result=scanner.processPoint(c,r,matrix);
                if (result!=null) {
                    coordinate=new Coordinate(hgt.getHeader().getCoordinate());
                    coordinate.getLatitude().setValue(coordinate.getLatitude().getValue()-1.0/(matrix.length-1)*r);
                    coordinate.getLongitude().setValue(coordinate.getLongitude().getValue()+1.0/(matrix[r].length-1)*c);
                    System.out.print(coordinate.toString());
                    System.out.print(": ");
                    for (IScanner.Direction dir : result.keySet())
                        System.out.print(dir+"("+result.get(dir)+"),");
                    System.out.println();
                }
            }
            */
    }
}
