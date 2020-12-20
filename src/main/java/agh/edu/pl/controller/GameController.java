package agh.edu.pl.controller;

import agh.edu.pl.biology.Animal;
import agh.edu.pl.executable.World;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private World world;
    private int size;
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

    public void setWorld(World world) {
        this.world = world;
        initGridPane();
        execution();
    }

    public void initGridPane(){
        this.size = Math.min(world.getSizeX(), world.getSizeY());
        this.entitySize = 700.0/size;

        for(int i=0; i<size; i++){
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPrefWidth(entitySize);
            gridPane.getColumnConstraints().add(columnConstraints);

            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPrefHeight(entitySize);
            gridPane.getRowConstraints().add(rowConstraints);
        }

        loadGridObjects();
    }

    public void loadGridObjects(){
        gridPane.getChildren().removeAll(nodes);
        nodes.clear();

        world.getOccupiedPositions().forEach(point -> {
            Circle circle = new Circle(entitySize/3);
            GridPane.setHalignment(circle, HPos.CENTER);
            circle.setFill(Color.AQUA);
            nodes.add(circle);
            gridPane.add(circle, point.getX(), point.getY());

            List<Animal> animals = world.getAnimalsByPosition(point);
            if(animals.size() > 1){
                Text text = new Text(Integer.toString(animals.size()));
                text.setFont(Font.font("Verdana", entitySize/3));
                GridPane.setHalignment(text, HPos.CENTER);
                nodes.add(text);
                gridPane.add(text, point.getX(), point.getY());
            }
            else if(animals.size() == 1){
                if(animals.get(0).getEnergy() <= world.getMinCopulateEnergy()) circle.setFill(Color.RED);
                else if(animals.get(0).getEnergy() <= world.getMinCopulateEnergy()*2) circle.setFill(Color.ORANGE);
            }
        });

        world.getOverGrownPositions().forEach(point -> {
            Rectangle rectangle = new Rectangle(entitySize, entitySize);
            rectangle.setFill(Color.GREEN);
            GridPane.setHalignment(rectangle, HPos.CENTER);
            nodes.add(rectangle);
            gridPane.add(rectangle, point.getX(), point.getY());
        });
    }

    public void execution(){
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(now - prevTime < sleepTime) return;
                prevTime = now;

                world.nextDay();
                loadGridObjects();
            }
        };

        animationTimer.start();
    }
}
