package agh.edu.pl.biology;

import agh.edu.pl.world.IWorldService;
import agh.edu.pl.geography.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Animal implements Comparable<Animal>{
    private static int maxId = 0;
    private final ArrayList<Animal> children;
    private final IWorldService iWorldService;
    private final Genom genom;
    private final int copulationEnergy;
    private final int startEpochNumber;
    private final int id;
    private Orientation orientation;
    private Point location;
    private int energy;
    private int epochs;

    public Animal(IWorldService iWorldService, Point location, int energy, Genom genom, int startEpochNumber) {
        this.children = new ArrayList<>();
        this.iWorldService = iWorldService;
        this.genom = genom;
        this.orientation = Orientation.values()[genom.getRandomGene()];
        this.location = location;
        this.energy = energy;
        this.copulationEnergy = energy/2;
        this.startEpochNumber = startEpochNumber;
        this.epochs = 0;
        this.id = maxId++;
    }

    public Animal(IWorldService iWorldService, Point location){
        this(iWorldService, location, Energy.startValue, new Genom(), 0);
    }

    public boolean isAlive(){
        return energy > 0;
    }

    public void eat(int energy){
        this.energy += energy;
    }

    public void rotate(){
        orientation = orientation.rotate(genom.getRandomGene());
    }

    public void move(){
        if(isAlive()){
            Point oldPosition = location;
            location = iWorldService.getCorrectPosition(location.add(orientation.asUnitVector()));
            iWorldService.changePosition(oldPosition, this);
            this.energy -= Energy.moveValue;

            if(!isAlive()) iWorldService.reportDeath(this);
        }
        else{
            iWorldService.reportDeath(this);
        }
    }

    public static Animal copulate(Animal animal1, Animal animal2){
        if(!animal1.canCopulate() || !animal2.canCopulate())
            return null;

        int childEnergy = animal1.energy/4 + animal2.energy/4;
        animal1.energy -= animal1.energy/4;
        animal2.energy -= animal2.energy/4;

        Point childLocation = animal1.iWorldService.getClosePosition(animal1.location);
        Genom childGenom = new Genom(animal1.genom, animal2.genom);
        Animal child = new Animal(animal1.iWorldService, childLocation, childEnergy, childGenom,
                animal1.startEpochNumber + animal1.getEpochs());
        animal1.addChild(child);
        animal2.addChild(child);
        return child;
    }

    public void addChild(Animal child){
        children.add(child);
    }

    public void nextEpoch(){
        epochs++;
    }

    public Point getLocation() {
        return location;
    }

    public int getEnergy() {
        return energy;
    }

    public int getCopulationEnergy() {
        return copulationEnergy;
    }

    public Genom getGenom() {
        return genom;
    }

    public int getEpochs() {
        return epochs;
    }

    public int getStartEpochNumber() {
        return startEpochNumber;
    }

    public int getId() {
        return id;
    }

    public List<Animal> getChildren() {
        return children;
    }

    public int getDescendantsNumber(){
        return children.size() + children.stream()
                .mapToInt(Animal::getDescendantsNumber)
                .sum();
    }

    private boolean canCopulate(){
        return energy >= copulationEnergy;
    }

    @Override
    public int compareTo(Animal animal) {
        return Integer.compare(energy, animal.energy);
    }

    @Override
    public String toString() {
        return "Animal " + id + " at " + location;
    }
}
