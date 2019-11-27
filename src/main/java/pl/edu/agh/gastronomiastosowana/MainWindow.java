package pl.edu.agh.gastronomiastosowana;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWindow extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream("/fxml/ProjectView.fxml"));
        primaryStage.setTitle("GastroFaza");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


    }
}
