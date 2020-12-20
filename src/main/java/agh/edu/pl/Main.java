package agh.edu.pl;

import agh.edu.pl.controller.GameController;
import agh.edu.pl.executable.Creator;
import agh.edu.pl.executable.DataProvider;
import agh.edu.pl.geography.Zone;
import agh.edu.pl.world.World;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GameController.class.getResource("../../../../view/gameView.fxml"));
            BorderPane layout = loader.load();

            Zone jungle = Creator.createJungle();
            Zone territory = Creator.createMap(jungle);
            World world = Creator.createWorld(territory);
            world.init(20,20);
            DataProvider dataProvider = new DataProvider(
                    world, territory, jungle
            );

            GameController controller = loader.getController();
            controller.setDataProvider(dataProvider);

            Scene scene = new Scene(layout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Evolution");
            primaryStage.minWidthProperty().bind(layout.minWidthProperty());
            primaryStage.minHeightProperty().bind(layout.minHeightProperty());
            primaryStage.maxWidthProperty().bind(layout.maxWidthProperty());
            primaryStage.maxHeightProperty().bind(layout.maxHeightProperty());
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
