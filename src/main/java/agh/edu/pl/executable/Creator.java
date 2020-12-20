package agh.edu.pl.executable;

import agh.edu.pl.biology.Energy;
import agh.edu.pl.geography.Jungle;
import agh.edu.pl.geography.Point;
import agh.edu.pl.geography.Territory;
import agh.edu.pl.geography.Zone;
import agh.edu.pl.params.Input;
import agh.edu.pl.world.World;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

// @TODO package access + createWorld refactor => tests refactor
public class Creator {
    private static Input input = null;

    public static void init(){
        try{
            input = new Gson().fromJson(
                            new JsonReader(
                                    new FileReader("src/main/resources/settings/settings.json")
                            ), Input.class
                    );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Energy.startValue = input.startEnergy;
        Energy.moveValue = input.moveEnergy;
        Energy.plantValue = input.plantEnergy;
    }

    public static Zone createJungle(){
        if(input == null) init();
        return new Jungle(
                new Point((int)((input.width-1) * (1-input.jungleRatio)/2),(int)((input.height-1) * (1-input.jungleRatio)/2)),
                new Point((int)((input.width-1) * (1+input.jungleRatio)/2),(int)((input.height-1) * (1+input.jungleRatio)/2))
        );
    }

    public static Zone createMap(Zone ... zones){
        if(input == null) init();
        return new Territory(
                new Point(0,0),
                new Point(input.width-1,input.height-1),
                zones
        );
    }

    public static World createWorld(Zone map){
        if(input == null) init();
        return new World(map);
    }
}
