package org.ems.model;

import org.ems.visualize.PngBuilder;

/**
* Created by stas on 6/22/14.
*/
public class MatrixCoordinate {
    public int x,y;

    public MatrixCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setValues(int x, int y) {
        this.x=x;
        this.y=y;
    }

    public MatrixCoordinate() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MatrixCoordinate that = (MatrixCoordinate) o;

        if (x != that.x) return false;
        if (y != that.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
