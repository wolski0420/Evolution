package agh.edu.pl.observation;

import agh.edu.pl.biology.Animal;
import agh.edu.pl.executable.DataProvider;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private final List<Integer> animalsNumberHistory;
    private final List<Integer> plantsNumberHistory;
    private final List<Integer> dominantGeneHistory;
    private final List<Double> averageEnergyHistory;
    private final List<Double> averageLifeTimeHistory;
    private final List<Double> averageChildNumberHistory;

    public Statistics(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        epochNumber = new SimpleIntegerProperty(0);
        actualAnimalsNumber = new SimpleIntegerProperty(0);
        actualPlantsNumber = new SimpleIntegerProperty(0);
        actualDominantGene = new SimpleIntegerProperty(0);
        averageEnergy = new SimpleDoubleProperty(0);
        averageLifeTime = new SimpleDoubleProperty(0);
        averageChildNumber = new SimpleDoubleProperty(0);
        animalsNumberHistory = new ArrayList<>();
        plantsNumberHistory = new ArrayList<>();
        dominantGeneHistory = new ArrayList<>();
        averageEnergyHistory = new ArrayList<>();
        averageLifeTimeHistory = new ArrayList<>();
        averageChildNumberHistory = new ArrayList<>();
    }

    @Override
    public void update() {
        epochNumber.setValue(dataProvider.getEpochNumber());

        int animalsNumber = dataProvider.getAnimals().size();
        actualAnimalsNumber.setValue(animalsNumber);
        animalsNumberHistory.add(animalsNumber);

        int plantsNumber = dataProvider.getOverGrownPositions().size();
        actualPlantsNumber.setValue(plantsNumber);
        plantsNumberHistory.add(plantsNumber);

        dataProvider.getAnimals().stream()
                .flatMapToInt(animal -> Arrays.stream(animal.getGenom().getGenes()))
                .boxed()
                .collect(Collectors.groupingBy(num -> num, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .ifPresentOrElse(
                        integerLongEntry -> {
                            actualDominantGene.setValue(integerLongEntry.getKey());
                            dominantGeneHistory.add(integerLongEntry.getKey());
                        },
                        () -> {
                            actualDominantGene.setValue(-1);
                            dominantGeneHistory.add(-1);
                        }
                );

        double avgEnergy = dataProvider.getAverageEnergy(dataProvider.getAnimals());
        averageEnergy.setValue(avgEnergy);
        averageEnergyHistory.add(avgEnergy);

        dataProvider.getDeadAnimals().stream()
                .mapToInt(Animal::getEpochs)
                .average()
                .ifPresentOrElse(
                        (value) -> {
                            averageLifeTime.setValue(value);
                            averageLifeTimeHistory.add(value);
                        },
                        () -> {
                            averageLifeTime.setValue(0);
                            averageLifeTimeHistory.add(0.0);
                        }
                );

        dataProvider.getAnimals().stream()
                .mapToInt(animal -> animal.getChildren().size())
                .average()
                .ifPresentOrElse(
                        (value) -> {
                            averageChildNumber.setValue(value);
                            averageChildNumberHistory.add(value);
                        },
                        () -> {
                            averageChildNumber.setValue(0);
                            averageChildNumberHistory.add(0.0);
                        }
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

    public double getAverageAnimalsNumberHistory(int from, int to){
        return animalsNumberHistory.subList(from, to).stream()
                .mapToInt(v -> v)
                .average()
                .orElse(0);
    }

    public double getAveragePlantsNumberHistory(int from, int to){
        return plantsNumberHistory.subList(from, to).stream()
                .mapToInt(v -> v)
                .average()
                .orElse(0);
    }

    public double getAverageDominantGeneHistory(int from, int to){
        return dominantGeneHistory.subList(from, to).stream()
                .filter(g -> g!=-1)
                .mapToInt(v -> v)
                .average()
                .orElse(0);
    }

    public double getAverageEnergyHistory(int from, int to){
        return averageEnergyHistory.subList(from, to).stream()
                .mapToDouble(v -> v)
                .average()
                .orElse(0);
    }

    public double getAverageLifetimeHistory(int from, int to){
        return averageLifeTimeHistory.subList(from, to).stream()
                .filter(v -> v!=0.0)
                .mapToDouble(v -> v)
                .average()
                .orElse(0);
    }

    public double getAverageChildrenNumberHistory(int from, int to){
        return averageChildNumberHistory.subList(from, to).stream()
                .filter(v -> v!=0.0)
                .mapToDouble(v -> v)
                .average()
                .orElse(0);
    }
}
