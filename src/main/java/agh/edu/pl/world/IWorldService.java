package agh.edu.pl.world;

import agh.edu.pl.biology.Animal;
import agh.edu.pl.geography.Point;

public interface IWorldService {
    Point getCorrectPosition(Point point);

    Point getClosePosition(Point point);

    void changePosition(Point oldPosition, Animal animal);

    void reportDeath(Animal animal);
}
