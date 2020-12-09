package agh.edu.pl.biology;

import agh.edu.pl.executable.IWorldService;
import agh.edu.pl.geography.Point;

public class Animal implements Comparable<Animal>{
    private final IWorldService iWorldService;
    private final Genom genom;
    private Orientation orientation;
    private Point location;
    private int energy;

    public Animal(IWorldService iWorldService, Point location, int energy, Genom genom) {
        this.iWorldService = iWorldService;
        this.genom = genom;
        this.orientation = Orientation.values()[genom.getRandomGene()];
        this.location = location;
        this.energy = energy;
    }

    public Animal(IWorldService iWorldService, Point location, int energy){
        this(iWorldService, location, energy, new Genom());
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

    public void move(int costEnergy){
        if(isAlive()){
            Point oldPosition = location;
            location = iWorldService.getCorrectPosition(location.add(orientation.asUnitVector()));
            iWorldService.changePosition(oldPosition, this);
            this.energy -= costEnergy;
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
        return new Animal(animal1.iWorldService, childLocation, childEnergy, childGenom);
    }

    public Point getLocation() {
        return location;
    }

    public int getEnergy() {
        return energy;
    }

    private boolean canCopulate(){
        return energy >= iWorldService.getMinCopulateEnergy();
    }

    @Override
    public int compareTo(Animal animal) {
        return Integer.compare(energy, animal.energy);
    }

    @Override
    public String toString() {
        return "Animal{" +
                "genom=" + genom +
                ", orientation=" + orientation +
                ", location=" + location +
                ", energy=" + energy +
                '}';
    }
}
