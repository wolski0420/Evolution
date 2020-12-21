package agh.edu.pl.observation;

import agh.edu.pl.biology.Animal;
import agh.edu.pl.executable.DataProvider;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Statistics implements IObserver {
    private final DataProvider dataProvider;
    private final IntegerProperty epochNumber;
    private final IntegerProperty actualAnimalsNumber;
    private final IntegerProperty actualPlantsNumber;
    private final IntegerProperty actualDominantGene;
    private final DoubleProperty averageEnergy;
    private final DoubleProperty averageLifeTime;
    private final DoubleProperty averageChildNumber;

    public Statistics(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        epochNumber = new SimpleIntegerProperty(0);
        actualAnimalsNumber = new SimpleIntegerProperty(0);
        actualPlantsNumber = new SimpleIntegerProperty(0);
        actualDominantGene = new SimpleIntegerProperty(0);
        averageEnergy = new SimpleDoubleProperty(0);
        averageLifeTime = new SimpleDoubleProperty(0);
        averageChildNumber = new SimpleDoubleProperty(0);
    }

    @Override
    public void update() {
        epochNumber.setValue(dataProvider.getEpochNumber());

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

        averageEnergy.setValue(dataProvider.getAverageEnergy(
                dataProvider.getAnimals()
        ));

        dataProvider.getDeadAnimals().stream()
                .mapToInt(Animal::getEpochs)
                .average()
                .ifPresentOrElse(
                        averageLifeTime::setValue,
                        () -> averageLifeTime.setValue(0)
                );

        dataProvider.getAnimals().stream()
                .mapToInt(animal -> animal.getChildren().size())
                .average()
                .ifPresentOrElse(
                        averageChildNumber::setValue,
                        () -> averageChildNumber.setValue(0)
                );
    }

    public IntegerProperty epochNumberProperty() {
        return epochNumber;
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

    public DoubleProperty averageEnergyProperty() {
        return averageEnergy;
    }

    public DoubleProperty averageLifeTimeProperty() {
        return averageLifeTime;
    }

    public DoubleProperty averageChildNumberProperty() {
        return averageChildNumber;
    }
}
