package org.ems;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.ems.model.Coordinate;
import org.ems.model.Statistics;
import org.ems.model.hgt.HGT;
import org.ems.scanners.Direction;
import org.ems.scanners.ThresholdScanner;
import org.ems.visualize.KmlBuilder;
import org.ems.visualize.PngUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Stas Shimov <stas.shimov@gmail.com>
 * Date: Oct 15, 2007
 * Time: 12:47:01 AM
 */
public class CmdLineApp {
    public static void main(String[] args) throws Exception {


        HGT hgt=HGT.create("src/main/resources/hgt/N47E030.hgt");
        if (hgt==null) return;

        ThresholdScanner scanner=new ThresholdScanner(hgt); //meters
        KmlBuilder kml=new KmlBuilder();
        for (Direction direction : Direction.values()) {
            Statistics stat=scanner.diffForDirection(direction);
            kml.addDirection(direction, scanner.scan(3, 70, direction));
        }
        System.out.println(kml.build());
//        PngUtils.createPng(hgt.getHeightsMatrix(), new File("output.png"), stat.getMinHeight(), stat.getMaxHeight());



    }

}
