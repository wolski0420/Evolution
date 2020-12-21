package agh.edu.pl.world;

import agh.edu.pl.biology.Animal;
import agh.edu.pl.biology.Energy;
import agh.edu.pl.biology.Orientation;
import agh.edu.pl.geography.Point;
import agh.edu.pl.geography.Zone;
import agh.edu.pl.observation.Observable;
import com.google.common.collect.ArrayListMultimap;

import java.util.*;
import java.util.stream.Collectors;

public class World extends Observable implements IWorldService{
    private final static int randAttempts = 10;
    private final ArrayList<Animal> bornAnimals;
    private final ArrayList<Animal> deadAnimals;
    private final ArrayList<Animal> listOfAnimals;
    private final ArrayListMultimap<Point, Animal> mapOfAnimals;
    private final Random random;
    private final Zone map;
    private int epoch;

    public World(Zone map) {
        this.bornAnimals = new ArrayList<>();
        this.deadAnimals = new ArrayList<>();
        this.listOfAnimals = new ArrayList<>();
        this.mapOfAnimals = ArrayListMultimap.create();
        this.random = new Random();
        this.map = map;
        this.epoch = 0;
    }

    public void init(int animalsNumber, int plantsNumber){
        for(int k=0; k<animalsNumber; k++){
            boolean finish = false;
            for(int i=0; i<Math.sqrt(randAttempts) && !finish; i++){
                int x = random.nextInt(map.getLengthX());
                for(int j=0; j<Math.sqrt(randAttempts) && !finish; j++){
                    int y = random.nextInt(map.getLengthY());
                    Point newPoint = new Point(x, y);
                    if(!isOccupied(newPoint)){
                        Animal animal = new Animal(this, newPoint);
                        listOfAnimals.add(animal);
                        mapOfAnimals.put(newPoint, animal);
                        finish = true;
                    }
                }
            }
        }

        for(int i=0; i<plantsNumber/2; i++){
            map.randPlant(mapOfAnimals.keySet());
        }

//        updateAll();

        System.out.println("===================INIT====================");
        print();
    }

    public void nextDay(){
        removeCorpses();
        rotateAnimals();
        moveAnimals();
        animalsEat();
        copulation();
        map.randPlant(mapOfAnimals.keySet());
        greaterAnimalsEpoch();
        epoch++;

        updateAll();

        System.out.println("===================NEXTDAY==================");
        print();
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
        listOfAnimals.forEach(Animal::move);
    }

    private void animalsEat(){
        mapOfAnimals.keySet().forEach(key -> {
            if(map.isOverGrown(key)){
                List<Animal> mostEnergeticAnimals = getStrongestAnimalsAtPosition(key);
                mostEnergeticAnimals.forEach(animal -> animal.eat(Energy.plantValue /mostEnergeticAnimals.size()));
                map.removePlant(key);
            }
        });
    }

    private void copulation(){
        mapOfAnimals.keySet().forEach(key -> {
            List<Animal> strongestAnimals = getStrongestAnimalsAtPosition(key);

            // when there is only one strongest animal at the same position, I'm adding the second strongest
            if(strongestAnimals.size() == 1){
                mapOfAnimals.get(key).stream()
                        .filter(animal -> !animal.equals(strongestAnimals.get(0)))
                        .max(Animal::compareTo)
                        .ifPresent(strongestAnimals::add);
            }

            // when there were two strongest animals with the same energy or only one and the second one was added
            if(strongestAnimals.size() >= 2) {
                Animal animal = Animal.copulate(strongestAnimals.remove(random.nextInt(strongestAnimals.size())),
                                                strongestAnimals.remove(random.nextInt(strongestAnimals.size())));
                if(animal != null){
                    bornAnimals.add(animal);
                }
            }
        });

        bornAnimals.forEach(animal -> {
            listOfAnimals.add(animal);
            mapOfAnimals.put(animal.getLocation(), animal);
        });

        bornAnimals.clear();
    }

    private void greaterAnimalsEpoch(){
        listOfAnimals.forEach(Animal::nextEpoch);
    }

    private List<Animal> getStrongestAnimalsAtPosition(Point point){
        return mapOfAnimals.get(point).stream()
                .filter(animal -> animal.getEnergy() == Collections.max(mapOfAnimals.get(point)).getEnergy())
                .collect(Collectors.toList());
    }

    public ArrayListMultimap<Point, Animal> getMapOfAnimals() {
        return mapOfAnimals;
    }

    public ArrayList<Animal> getListOfAnimals() {
        return listOfAnimals;
    }

    public ArrayList<Animal> getDeadAnimals() {
        return deadAnimals;
    }

    public int getEpoch() {
        return epoch;
    }

    public boolean isOccupied(Point point){
        return mapOfAnimals.containsKey(point);
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
    public void reportDeath(Animal animal) {
        deadAnimals.add(animal);
    }

    public void print(){
        System.out.println("---------------Plants-----------------");
        map.print();
        System.out.println("---------------Animals------------------");
        mapOfAnimals.keySet().forEach(key -> {
            System.out.println(key + ": ");
            mapOfAnimals.get(key).forEach(System.out::println);
        });
    }
}
