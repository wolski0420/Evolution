package agh.edu.pl.executable;

import agh.edu.pl.biology.Animal;
import agh.edu.pl.geography.Point;
import agh.edu.pl.geography.Zone;
import agh.edu.pl.world.World;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Set<Point> getOverGrownPositions(){
        return territory.getOverGrownPositions();
    }

    public Set<Point> getOccupiedPositions(){
        return world.getMapOfAnimals().keySet();
    }

    public List<Animal> getAnimals(){
        return world.getListOfAnimals();
    }

    public List<Animal> getDeadAnimals(){
        return world.getDeadAnimals();
    }

    public List<Animal> getAnimalsByPosition(Point position){
        return world.getMapOfAnimals().get(position);
    }

    public double getAverageEnergy(List<Animal> animals){
        return animals.stream()
                .mapToInt(Animal::getEnergy)
                .average()
                .orElse(0);
    }

    public double getAverageCopulationEnergy(List<Animal> animals){
        return animals.stream()
                .mapToInt(Animal::getCopulationEnergy)
                .average()
                .orElse(0);
    }

    public List<Pair<Point, Point>> getZonesBounds(){
        return Arrays.stream(zones)
                .map(zone -> new Pair<>(zone.getLeftLowerCorner(), zone.getRightUpperCorner()))
                .collect(Collectors.toList());
    }

    public int getEpochNumber(){
        return world.getEpoch();
    }

    public void nextDay(){
        world.nextDay();
    }
}
