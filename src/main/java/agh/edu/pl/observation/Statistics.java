package agh.edu.pl.observation;

import agh.edu.pl.executable.DataProvider;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Statistics implements IObserver {
    private final DataProvider dataProvider;
    private int sumOfDeaths;
    private final IntegerProperty actualAnimalsNumber;
    private final IntegerProperty actualPlantsNumber;
    private final IntegerProperty actualDominantGene;
    private final IntegerProperty averageEnergy;
    private final IntegerProperty averageLifeTime;
    private final IntegerProperty averageChildNumber;

    public Statistics(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        actualAnimalsNumber = new SimpleIntegerProperty(0);
        actualPlantsNumber = new SimpleIntegerProperty(0);
        actualDominantGene = new SimpleIntegerProperty(0);
        averageEnergy = new SimpleIntegerProperty(0);
        averageLifeTime = new SimpleIntegerProperty(0);
        averageChildNumber = new SimpleIntegerProperty(0);
        sumOfDeaths = 0;
    }

    @Override
    public void update() {
        actualAnimalsNumber.setValue(dataProvider.getAnimals().size());
        actualPlantsNumber.setValue(dataProvider.getOverGrownPositions().size());

        dataProvider.getAnimals().stream()
                .flatMapToInt(animal -> Arrays.stream(animal.getGenom().getGenes()))
                .boxed()
                .collect(Collectors.groupingBy(num -> num, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(integerLongEntry -> actualDominantGene.setValue(integerLongEntry.getKey()));


    }

    public IntegerProperty actualAnimalsNumberProperty() {
        return actualAnimalsNumber;
    }

    public IntegerProperty actualPlantsNumberProperty() {
        return actualPlantsNumber;
    }

    public IntegerProperty actualDominantGeneProperty() {
        return actualDominantGene;
    }

    public IntegerProperty averageEnergyProperty() {
        return averageEnergy;
    }

    public IntegerProperty averageLifeTimeProperty() {
        return averageLifeTime;
    }

    public IntegerProperty averageChildNumberProperty() {
        return averageChildNumber;
    }
}
