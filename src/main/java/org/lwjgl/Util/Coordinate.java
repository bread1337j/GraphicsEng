package org.lwjgl.Util;

import java.util.Arrays;
import java.util.Objects;

public class Coordinate {
    public int[] cord; //0=x 1=y
    public Coordinate(int[] cord) {
        this.cord = cord;
    }
    public Coordinate(int x, int y){
        this.cord = new int[] {x,y};
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return Objects.deepEquals(cord, that.cord);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(cord);
    }
}
