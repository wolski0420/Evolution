package agh.edu.pl.observation;

import agh.edu.pl.biology.Animal;
import agh.edu.pl.observation.IObserver;

import java.util.ArrayList;

// @TODO
public class Statistics implements IObserver {
    private final ArrayList<Animal> listOfAnimals;

    public Statistics(ArrayList<Animal> listOfAnimals) {
        this.listOfAnimals = listOfAnimals;
    }

    @Override
    public void update() {
        // @TODO
    }
}
