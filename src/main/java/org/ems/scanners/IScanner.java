package org.ems.scanners;

import org.ems.model.Direction;
import org.ems.model.GeoCoordinate;
import org.ems.model.hgt.HGT;

import java.util.List;
import java.util.Map;

/**
 * User: Stas Shimov <stas.shimov@gmail.com>
 * Date: Sep 27, 2008
 * Time: 3:48:36 PM
 */
public interface IScanner {

    List<GeoCoordinate> scan(HGT hgt, int threshold);


    Map<Direction,Integer> processPoint(int x, int y, int[][] matrix);
}
