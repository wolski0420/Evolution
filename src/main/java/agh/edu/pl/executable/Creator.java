package agh.edu.pl.executable;

import agh.edu.pl.biology.Energy;
import agh.edu.pl.geography.Jungle;
import agh.edu.pl.geography.Point;
import agh.edu.pl.geography.Territory;
import agh.edu.pl.geography.Zone;
import agh.edu.pl.world.World;

// @TODO package access + createWorld refactor => tests refactor
// @TODO JSON reader (only once, with boolean check)
public class Creator {
    public static Zone createMap(int width, int height, double jungleRatio){
        return new Territory(new Point(0,0), new Point(width-1,height-1), new Jungle(
                new Point((int)((width-1) * (1-jungleRatio)/2),(int)((height-1) * (1-jungleRatio)/2)),
                new Point((int)((width-1) * (1+jungleRatio)/2),(int)((height-1) * (1+jungleRatio)/2)))
        );
    }

    public static void setEnergy(int startEnergy, int moveEnergy, int plantEnergy){
        Energy.startValue = startEnergy;
        Energy.moveValue = moveEnergy;
        Energy.plantValue = plantEnergy;
    }

    public static World createWorld(Zone map){
        return new World(map);
    }
}
