package agh.edu.pl.executable;

import agh.edu.pl.biology.Animal;
import agh.edu.pl.biology.Orientation;
import agh.edu.pl.geography.Jungle;
import agh.edu.pl.geography.Point;
import agh.edu.pl.geography.Territory;
import agh.edu.pl.geography.Zone;
import agh.edu.pl.observation.Observable;
import com.google.common.collect.TreeMultimap;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

//@TODO
public class World extends Observable implements IWorldService{
    private final ArrayList<Animal> deadAnimals;
    private final ArrayList<Animal> listOfAnimals;
    private final TreeMultimap<Point, Animal> mapOfAnimals;
    private final Random random;
    private final Zone map;
    private final int moveEnergy;

    public World(int energy) {
        deadAnimals = new ArrayList<>();
        listOfAnimals = new ArrayList<>();
        mapOfAnimals = TreeMultimap.create();
        random = new Random();
        map = new Territory(new Point(0,0), new Point(8,8), new Jungle(new Point(3,3), new Point(5,5)));
        moveEnergy = energy;
    }

    private void removeCorpses(){
        deadAnimals.forEach(animal -> {
            listOfAnimals.remove(animal);
            mapOfAnimals.remove(animal.getLocation(), animal);
        });

        deadAnimals.clear();
    }

    private void rotateAnimals(){
        listOfAnimals.forEach(Animal::rotate);
    }

    private void moveAnimals(){
        listOfAnimals.forEach(animal -> animal.move(moveEnergy));
    }

    private void animalsEat(){
        mapOfAnimals.keySet().forEach(key -> {
            // @TODO comparator by energy for animals ??
        });
    }

    @Override
    public Point getCorrectPosition(Point point) {
        if(map.isInBounds(point)) return point;
        return new Point((point.getX() + map.getLengthX()) % map.getLengthX(),
                        (point.getY() + map.getLengthY()) % map.getLengthY());
    }

    @Override
    public Point getClosePosition(Point point) {
        for(Orientation orientation : Orientation.values()){
            Point proposal = getCorrectPosition(point.add(orientation.asUnitVector()));
            if(!isOccupied(proposal))
                return proposal;
        }

        return getCorrectPosition(point.add(Orientation.values()[random.nextInt(8)].asUnitVector()));
    }

    @Override
    public void changePosition(Point oldPosition, Animal animal) {
        mapOfAnimals.remove(oldPosition, animal);
        mapOfAnimals.put(animal.getLocation(), animal);
    }

    @Override
    public void removeDeadAnimal(Animal animal) {
        deadAnimals.add(animal);
    }

    public ArrayList<Animal> getListOfAnimals() {
        return listOfAnimals;
    }

    public boolean isOccupied(Point point){
        return mapOfAnimals.containsKey(point);
    }
}
