package agh.edu.pl.controller;

import agh.edu.pl.biology.Animal;
import agh.edu.pl.executable.DataProvider;
import agh.edu.pl.observation.Statistics;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private DataProvider dataProvider;
    private Statistics statistics;
    private double entitySize;
    private final List<Node> nodes = new ArrayList<>();
    private AnimationTimer animationTimer;
    private long prevTime = 0;
    private final long sleepTime = 1000000000;

    @FXML
    public GridPane gridPane;

    @FXML
    public Button executionButton;

    @FXML
    public Label animalsNumberLabel;

    @FXML
    public void initialize(){
        executionButton.setText("Stop");
        executionButton.setCancelButton(true);
        executionButton.setStyle("-fx-background-color: #fd2b2b");

        executionButton.setOnAction(e -> {
            if(executionButton.getText().equals("Stop")){
                animationTimer.stop();
                executionButton.setText("Start");
                executionButton.setStyle("-fx-background-color: #00c700");
            }
            else{
                animationTimer.start();
                executionButton.setText("Stop");
                executionButton.setStyle("-fx-background-color: #fd2b2b");
            }
        });
    }

    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        initGridPane();
        execution();
    }

    public void setStatistics(Statistics statistics){
        this.statistics = statistics;
        initStatistics();
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

    private void execution(){
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(now - prevTime < sleepTime) return;
                prevTime = now;

                dataProvider.nextDay();
                loadGridObjects();
            }
        };

        animationTimer.start();
    }

    public void stop(){
        animationTimer.stop();
    }

    private void initStatistics(){
//        animalsNumberLabel.textProperty().bind();
    }
}
