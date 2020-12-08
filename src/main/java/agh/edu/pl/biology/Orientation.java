package agh.edu.pl.biology;

import agh.edu.pl.geography.Point;

public enum Orientation {
    NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;

    public Orientation rotate(int iter){
        return values()[(this.ordinal() + iter) % values().length];
    }

    public Point asUnitVector(){
        if(this==NORTH) return new Point(0, 1);
        if(this==NORTH_EAST) return new Point(1, 1);
        if(this==EAST) return new Point(1, 0);
        if(this==SOUTH_EAST) return new Point(1, -1);
        if(this==SOUTH) return new Point(0, -1);
        if(this==SOUTH_WEST) return new Point(-1, -1);
        if(this==WEST) return new Point(-1, 0);
        if(this==NORTH_WEST) return new Point(-1, 1);
        return new Point(0,0);
    }
}
