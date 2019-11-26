package pl.edu.agh.gastronomiastosowana;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.edu.agh.gastronomiastosowana.controller.ProjectViewController;
import pl.edu.agh.gastronomiastosowana.dao.ParticipantDao;
import pl.edu.agh.gastronomiastosowana.model.Participant;

public class MainWindow extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream("/fxml/ProjectView.fxml"));
        primaryStage.setTitle("GastroFaza");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        ParticipantDao p = new ParticipantDao();
        Participant add = new Participant("new", "new", 12, "new");

        p.create(add);


    }
}
