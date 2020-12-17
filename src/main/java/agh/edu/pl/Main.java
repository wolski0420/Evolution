package agh.edu.pl;

import agh.edu.pl.controller.GameController;
import agh.edu.pl.executable.World;
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
        World world = new World(8, 8, 40,1, 5, 0.5);
        world.init(20,20);

        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GameController.class.getResource("../../../../view/gameView.fxml"));
            BorderPane layout = loader.load();

            GameController controller = loader.getController();
            controller.setWorld(world);

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
