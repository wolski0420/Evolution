package agh.edu.pl.controller;

import agh.edu.pl.executable.Creator;
import agh.edu.pl.executable.DataProvider;
import agh.edu.pl.geography.Zone;
import agh.edu.pl.observation.Statistics;
import agh.edu.pl.world.World;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartController {
    private final List<Stage> stages = new ArrayList<>();
    private static int counter = 0;

    @FXML
    public Spinner<Integer> animalsSpinner;
    @FXML
    public Spinner<Integer> plantsSpinner;

    @FXML
    public void handleStartButton(){
        try{
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GameController.class.getResource("../../../../view/gameView.fxml"));
            BorderPane layout = loader.load();

            Zone jungle = Creator.createJungle();
            Zone territory = Creator.createMap(jungle);
            World world = Creator.createWorld(territory);
            world.init(animalsSpinner.getValue(),plantsSpinner.getValue());
            DataProvider dataProvider = new DataProvider(
                    world, territory, jungle
            );
            Statistics statistics = new Statistics(dataProvider);
            world.addObserver(statistics);

            GameController controller = loader.getController();
            controller.setDataProvider(dataProvider);
            controller.setStatistics(statistics);
            controller.setStage(stage);
            controller.startGenerator();

            Scene scene = new Scene(layout);
            stages.add(stage);
            stage.setScene(scene);
            stage.setTitle("Game " + counter++);
            stage.setOnCloseRequest(event -> controller.stopGenerator());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeAllStages(){
        stages.forEach(Stage::close);
    }
}
