package agh.edu.pl;
import agh.edu.pl.geography.Jungle;
import agh.edu.pl.geography.Point;
import agh.edu.pl.geography.Territory;
import agh.edu.pl.geography.Zone;

import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        Zone jungle = new Jungle(new Point(3,3), new Point(5,5));
        Zone territory = new Territory(new Point(0,0), new Point(8,8), jungle);
        territory.print();
        jungle.print();
        HashSet<Point> points = new HashSet<>();
        points.add(new Point(3,3));
        points.add(new Point(1,1));
        points.add(new Point(5,5));
        points.add(new Point(7,7));
        territory.randPlants(points);
        territory.print();
        jungle.print();
        territory.randPlants(points);
        territory.print();
        jungle.print();
        territory.randPlants(points);
        territory.print();
        jungle.print();
        territory.randPlants(points);
        territory.print();
        jungle.print();
        territory.randPlants(points);
        territory.print();
        jungle.print();

        System.out.println(territory.isOverGrown(new Point(4,4)));
    }
}
