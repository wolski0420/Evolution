package agh.edu.pl.geography;

public class Jungle extends Zone{
    public Jungle(Point rightUpperCorner, Point leftLowerCorner) {
        super(rightUpperCorner, leftLowerCorner);
    }

    @Override
    public boolean isOverGrown(Point point) {
        return plants.contains(point);
    }

    @Override
    protected boolean canPlant(Point point) {
        return !isOverGrown(point);
    }
}
