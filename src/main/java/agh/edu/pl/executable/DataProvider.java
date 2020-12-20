package agh.edu.pl.executable;

import agh.edu.pl.biology.Animal;
import agh.edu.pl.geography.Point;
import agh.edu.pl.geography.Zone;
import agh.edu.pl.world.World;

import java.util.List;
import java.util.Set;

public class DataProvider {
    private final World world;
    private final Zone territory;
    private final Zone[] zones;

    public DataProvider(World world, Zone territory, Zone ... zones) {
        this.world = world;
        this.territory = territory;
        this.zones = zones;
    }

    public int getMapSizeX(){
        return territory.getLengthX();
    }

    public int getMapSizeY(){
        return territory.getLengthY();
    }

    public Set<Point> getOccupiedPositions(){
        return world.getMapOfAnimals().keySet();
    }

    public List<Animal> getAnimalsByPosition(Point position){
        return world.getMapOfAnimals().get(position);
    }

    public Set<Point> getOverGrownPositions(){
        return territory.getOverGrownPositions();
    }

    public double getAverageEnergy(List<Animal> animals){
        return (double) animals.stream()
                .map(Animal::getEnergy)
                .reduce(0, Integer::sum)
                /animals.size();
    }

    public double getAverageCopulationEnergy(List<Animal> animals){
        return (double) animals.stream()
                .map(Animal::getCopulationEnergy)
                .reduce(0, Integer::sum)
                /animals.size();
    }

    public void nextDay(){
        world.nextDay();
    }
}
