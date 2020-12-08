package agh.edu.pl.geography;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Territory extends Zone{
    private final List<Zone> zones;

    public Territory(Point rightUpperCorner, Point leftLowerCorner, Zone ... zones){
        super(rightUpperCorner, leftLowerCorner);
        this.zones = Arrays.asList(zones);
    }

    private boolean isOutsideTheZones(Point point){
        return zones.stream().noneMatch(zone -> zone.plants.contains(point));
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
    public void randPlants(Set<Point> occupiedFields) {
        super.randPlants(occupiedFields);
        zones.forEach(zone -> zone.randPlants(occupiedFields));
    }
}
