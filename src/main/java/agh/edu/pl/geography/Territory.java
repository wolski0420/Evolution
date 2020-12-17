package agh.edu.pl.geography;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Territory extends Zone{
    private final List<Zone> zones;

    public Territory(Point rightUpperCorner, Point leftLowerCorner, Zone ... zones){
        super(rightUpperCorner, leftLowerCorner);
        this.zones = Arrays.asList(zones);
    }

    private boolean isOutsideTheZones(Point point){
        return zones.stream().noneMatch(zone -> zone.isInBounds(point));
    }

    @Override
    public boolean isOverGrown(Point point) {
        return zones.stream().anyMatch(zone -> zone.isOverGrown(point)) || plants.contains(point);
    }

    @Override
    protected boolean canPlant(Point point) {
        return isOutsideTheZones(point) && !plants.contains(point);
    }

    @Override
    public void randPlant(Set<Point> occupiedFields) {
        zones.forEach(zone -> zone.randPlant(occupiedFields));
        super.randPlant(occupiedFields);
    }

    @Override
    public void removePlant(Point point) {
        super.removePlant(point);
        zones.forEach(zone -> zone.removePlant(point));
    }

    @Override
    public Set<Point> getOverGrownPositions() {
        return new HashSet<>(){
            {
                addAll(zones.stream()
                        .flatMap(zone -> zone.getOverGrownPositions().stream())
                        .collect(Collectors.toSet()));
                addAll(plants);
            }
        };
    }

    public void print(){
        System.out.println("=======" + getClass());
        plants.forEach(System.out::println);
        zones.forEach(Zone::print);
    }
}
