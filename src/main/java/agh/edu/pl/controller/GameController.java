package agh.edu.pl.controller;

import agh.edu.pl.biology.Animal;
import agh.edu.pl.executable.DataProvider;
import agh.edu.pl.observation.Statistics;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameController {
    private DataProvider dataProvider;
    private Statistics statistics;
    private double entitySize;
    private final List<Node> nodes = new ArrayList<>();
    private AnimationTimer generator;
    private long prevTime = 0;
    private final long sleepTime = 1000000000;
    @FXML
    public GridPane gridPane;
    @FXML
    public Button executionButton;
    @FXML
    public Label epochNumberLabel;
    @FXML
    public Label animalsNumberLabel;
    @FXML
    public Label plantsNumberLabel;
    @FXML
    public Label dominantGeneLabel;
    @FXML
    public Label avgEnergyLabel;
    @FXML
    public Label avgLifeTimeLabel;
    @FXML
    public Label avgChildNumberLabel;
    @FXML
    public ListView<Animal> animalsListView;
    @FXML
    public Label animalLabel;
    @FXML
    public Label animalGenesLabel;
    @FXML
    public Label animalEnergyLabel;
    @FXML
    public Label animalEpochsLabel;
    @FXML
    public Label animalChildrenLabel;
    @FXML
    public Label animalDescendantsLabel;

    @FXML
    public void initialize(){
        setGenerator();
        setExecutionButton();
        setAnimalsView();
    }

    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        initGridPane();
    }

    private void initGridPane(){
        int size = Math.max(dataProvider.getMapSizeX(), dataProvider.getMapSizeY());
        this.entitySize = 700.0/ size;

        gridPane.setMaxHeight(((double)700/size)*dataProvider.getMapSizeY());
        gridPane.setMaxWidth(((double)700/size)*dataProvider.getMapSizeX());

        for(int i=0; i<dataProvider.getMapSizeX(); i++){
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setMaxWidth(entitySize);
            columnConstraints.setMinWidth(entitySize);
            gridPane.getColumnConstraints().add(columnConstraints);
        }

        for(int i=0; i<dataProvider.getMapSizeY(); i++){
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setMaxHeight(entitySize);
            rowConstraints.setMinHeight(entitySize);
            gridPane.getRowConstraints().add(rowConstraints);
        }

        dataProvider.getZonesBounds().forEach(pair -> {
            for(int x=pair.getKey().getX(); x<=pair.getValue().getX(); x++){
                for(int y=pair.getKey().getY(); y<=pair.getValue().getY(); y++){
                    Rectangle rectangle = new Rectangle(entitySize, entitySize);
                    rectangle.setFill(Color.LIGHTGREEN);
                    GridPane.setHalignment(rectangle, HPos.CENTER);
                    GridPane.setValignment(rectangle, VPos.CENTER);
                    gridPane.add(rectangle, x, y);
                }
            }
        });

        loadGridObjects();
    }

    private void loadGridObjects(){
        gridPane.getChildren().removeAll(nodes);
        nodes.clear();

        dataProvider.getOccupiedPositions().forEach(point -> {
            Circle circle = new Circle(entitySize/3);
            GridPane.setHalignment(circle, HPos.CENTER);
            GridPane.setValignment(circle, VPos.CENTER);
            nodes.add(circle);
            gridPane.add(circle, point.getX(), point.getY());

            List<Animal> animals = dataProvider.getAnimalsByPosition(point);

            if(animals.size() > 1){
                Text text = new Text(Integer.toString(animals.size()));
                text.setFont(Font.font("Verdana", entitySize/3));
                GridPane.setHalignment(text, HPos.CENTER);
                GridPane.setValignment(text, VPos.CENTER);
                nodes.add(text);
                gridPane.add(text, point.getX(), point.getY());
            }

            if(animals.size() >= 1){
                double avgEnergy = dataProvider.getAverageEnergy(animals);
                double avgCopulationEnergy = dataProvider.getAverageCopulationEnergy(animals);

                if(avgEnergy <= 0) circle.setFill(Color.BLACK);
                else if(avgEnergy <= avgCopulationEnergy) circle.setFill(Color.RED);
                else if(avgEnergy <= avgCopulationEnergy*2) circle.setFill(Color.ORANGE);
                else circle.setFill(Color.AQUA);
            }

            Tooltip tooltip = new Tooltip();
            tooltip.setText(dataProvider.getAnimalsByPosition(point).stream()
                    .map(animal -> Arrays.toString(animal.getGenom().getGenes()))
                    .collect(Collectors.joining("\n"))
            );
            Tooltip.install(circle, tooltip);
        });

        dataProvider.getOverGrownPositions().forEach(point -> {
            Rectangle rectangle = new Rectangle(entitySize, entitySize);
            rectangle.setFill(Color.GREEN);
            GridPane.setHalignment(rectangle, HPos.CENTER);
            GridPane.setValignment(rectangle, VPos.CENTER);
            nodes.add(rectangle);
            gridPane.add(rectangle, point.getX(), point.getY());
        });


    }

    private void setExecutionButton(){
        executionButton.setText("Stop");
        executionButton.setCancelButton(true);
        executionButton.setStyle("-fx-background-color: #fd2b2b");

        executionButton.setOnAction(e -> {
            if(executionButton.getText().equals("Stop")){
                stopGenerator();
                executionButton.setText("Start");
                executionButton.setStyle("-fx-background-color: #00c700");
                animalsListView.setDisable(false);
                animalLabel.setDisable(false);
                animalGenesLabel.setDisable(false);
                animalEnergyLabel.setDisable(false);
                animalEpochsLabel.setDisable(false);
                animalChildrenLabel.setDisable(false);
                animalDescendantsLabel.setDisable(false);
            }
            else{
                startGenerator();
                executionButton.setText("Stop");
                executionButton.setStyle("-fx-background-color: #fd2b2b");
                animalsListView.setDisable(true);
                animalLabel.setDisable(true);
                animalGenesLabel.setDisable(true);
                animalEnergyLabel.setDisable(true);
                animalEpochsLabel.setDisable(true);
                animalChildrenLabel.setDisable(true);
                animalDescendantsLabel.setDisable(true);
            }
        });
    }

    private void setGenerator(){
        generator = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(now - prevTime < sleepTime) return;
                prevTime = now;

                dataProvider.nextDay();
                loadGridObjects();
                loadAnimalsView();
            }
        };
    }

    public void startGenerator(){
        generator.start();
    }

    public void stopGenerator(){
        generator.stop();
    }

    public void setStatistics(Statistics statistics){
        this.statistics = statistics;
        initStatistics();
    }

    private void initStatistics(){
        epochNumberLabel.textProperty().bind(
                statistics.epochNumberProperty()
                        .asString("Epoch: %d")
        );
        animalsNumberLabel.textProperty().bind(
                statistics.actualAnimalsNumberProperty()
                        .asString("Animals number: %d")
        );
        plantsNumberLabel.textProperty().bind(
                statistics.actualPlantsNumberProperty()
                        .asString("Plants number: %d")
        );
        dominantGeneLabel.textProperty().bind(
                statistics.actualDominantGeneProperty()
                        .asString("Dominant gene: %d")
        );
        avgEnergyLabel.textProperty().bind(
                statistics.averageEnergyProperty()
                        .asString("Average energy: %.2f")
        );
        avgLifeTimeLabel.textProperty().bind(
                statistics.averageLifeTimeProperty()
                        .asString("Average lifetime: %.2f epochs")
        );
        avgChildNumberLabel.textProperty().bind(
                statistics.averageChildNumberProperty()
                        .asString("Average children number: %.2f")
        );
    }

    public void setAnimalsView(){
        animalsListView.setDisable(true);
        animalsListView.setCellFactory(lv -> new ListCell<>(){
            @Override
            protected void updateItem(Animal item, boolean empty) {
                super.updateItem(item, empty);
                if(item != null)
                    setText(item.toString());
            }
        });

        animalsListView.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> {
                    if(newValue == null){
                        animalLabel.setText("Animal - at (-,-)");
                        animalGenesLabel.setText("Genes: -");
                        animalEnergyLabel.setText("Energy: -");
                        animalEpochsLabel.setText("Age: -");
                        animalChildrenLabel.setText("Children number: -");
                        animalDescendantsLabel.setText("Descendants number: -");
                    }
                    else{
                        animalLabel.setText(newValue.toString());
                        animalGenesLabel.setText("Genom: " + Arrays.toString(newValue.getGenom().getGenes()));
                        animalEnergyLabel.setText("Energy: " + newValue.getEnergy());
                        animalEpochsLabel.setText("Age: " + newValue.getEpochs());
                        animalChildrenLabel.setText("Children number: " + newValue.getChildren().size());
                        animalDescendantsLabel.setText("Descendants number: " + newValue.getDescendantsNumber());
                    }
                })
        );
    }

    public void loadAnimalsView(){
        animalsListView.getItems().clear();
        animalsListView.getItems().addAll(dataProvider.getAnimals());
    }
}
