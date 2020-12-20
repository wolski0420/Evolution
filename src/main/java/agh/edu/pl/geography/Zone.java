package agh.edu.pl.geography;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class Zone {
    protected static final int randAttempts = 10;
    protected final HashSet<Point> plants;
    protected final Random random;
    protected final Point rightUpperCorner;
    protected final Point leftLowerCorner;

    protected Zone(Point leftLowerCorner, Point rightUpperCorner) {
        this.rightUpperCorner = rightUpperCorner;
        this.leftLowerCorner = leftLowerCorner;
        this.plants = new HashSet<>();
        this.random = new Random();
    }

    public Point getRightUpperCorner() {
        return rightUpperCorner;
    }

    public Point getLeftLowerCorner() {
        return leftLowerCorner;
    }

    public int getLengthX(){
        return rightUpperCorner.getX() - leftLowerCorner.getX() + 1;
    }

    public int getLengthY(){
        return rightUpperCorner.getY() - leftLowerCorner.getY() + 1;
    }

    protected int getFieldsNumber(){
        return getLengthX() * getLengthY();
    }

    public boolean isInBounds(Point point){
        return point.precedes(rightUpperCorner) && point.follows(leftLowerCorner);
    }

    public void randPlant(Set<Point> occupiedFields) {
        if(occupiedFields.size() == getFieldsNumber()) return;

        boolean finish = false;
        for(int i=0; i<Math.sqrt(randAttempts) && !finish; i++){
            int x = random.nextInt(getLengthX()) + leftLowerCorner.getX();
            for(int j=0; j<Math.sqrt(randAttempts) && !finish; j++){
                int y = random.nextInt(getLengthY()) + leftLowerCorner.getY();
                Point newPoint = new Point(x, y);
                if(!occupiedFields.contains(newPoint) && canPlant(newPoint)){
                    plants.add(newPoint);
                    finish = true;
                }
            }
        }
    }

    public void removePlant(Point point){
        plants.remove(point);
    }

    public Set<Point> getOverGrownPositions(){
        return plants;
    }

    protected abstract boolean canPlant(Point point);

    public abstract boolean isOverGrown(Point point);

    public void print(){
        System.out.println("=======" + getClass());
        plants.forEach(System.out::println);
    }
}
