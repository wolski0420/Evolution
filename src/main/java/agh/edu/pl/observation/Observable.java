package agh.edu.pl.observation;

import java.util.ArrayList;

public class Observable {
    private final ArrayList<IObserver> observers;

    public Observable() {
        this.observers = new ArrayList<>();
    }

    public void addObserver(IObserver iObserver){
        observers.add(iObserver);
    }

    public void updateAll(){
        observers.forEach(IObserver::update);
    }
}
