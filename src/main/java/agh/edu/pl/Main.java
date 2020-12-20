package agh.edu.pl;

import agh.edu.pl.controller.StartController;
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
            loader.setLocation(StartController.class.getResource("../../../../view/startView.fxml"));
            BorderPane layout = loader.load();

            StartController controller = loader.getController();

            Scene scene = new Scene(layout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Evolution");
            primaryStage.setOnCloseRequest(event -> controller.closeAllStages());
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
