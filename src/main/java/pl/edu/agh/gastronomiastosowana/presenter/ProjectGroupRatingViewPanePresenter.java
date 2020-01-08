package pl.edu.agh.gastronomiastosowana.presenter;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import pl.edu.agh.gastronomiastosowana.dao.ParticipantDao;
import pl.edu.agh.gastronomiastosowana.dao.RatingDao;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;
import pl.edu.agh.gastronomiastosowana.model.Rating;
import pl.edu.agh.gastronomiastosowana.model.aggregations.RatingList;
import pl.edu.agh.gastronomiastosowana.model.reports.Report;
import pl.edu.agh.gastronomiastosowana.model.reports.ReportGenerator;
import pl.edu.agh.gastronomiastosowana.model.reports.SimpleTextProjectGroupRatingsSummaryReportGenerator;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class ProjectGroupRatingViewPanePresenter extends AbstractPresenter {

    private ProjectGroup projectGroup;
    private RatingList ratingList;
    private RatingDao ratingDao;
    private ParticipantDao participantDao;

    @FXML private TableView<Rating> ratingsTableView;
    @FXML private Button editRatingButton;
    @FXML private Button removeRatingButton;
    @FXML private TableColumn<Rating, Double> valueTableColumn;
    @FXML private TableColumn<Rating, Double> maxTableColumn;
    @FXML private TableColumn<Rating, String> participantColumn;

    public void initialize() {
        ratingList = new RatingList();
        ratingDao = new RatingDao();
        participantDao = new ParticipantDao();

        bindTableProperties();
        bindButtonProperties();
        bindColumnsValueFactory();
    }

    private void bindTableProperties() {
        ratingsTableView.itemsProperty().bind(ratingList.getProperty());
    }

    private void bindButtonProperties() {
        BooleanBinding disableBinding = ratingsTableView.getSelectionModel().selectedItemProperty().isNull();
        editRatingButton.disableProperty().bind(disableBinding);
        removeRatingButton.disableProperty().bind(disableBinding);
    }

    private void bindColumnsValueFactory() {
        valueTableColumn.setCellValueFactory(p -> p.getValue().getRatingDetails().ratingValueProperty().asObject());
        maxTableColumn.setCellValueFactory(p -> p.getValue().getRatingDetails().maxRatingValueProperty().asObject());
        participantColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getParticipant().getFullName()));
    }

    private void loadRatingList() {
        ratingList.setElements(FXCollections.observableList(ratingDao.findByProjectGroup(projectGroup)));
    }

    public void setProjectGroup(ProjectGroup selectedGroup) {
        this.projectGroup = selectedGroup;
        loadRatingList();
    }

    @FXML
    public void editRating() {
        Rating selectedRating = ratingsTableView.getSelectionModel().getSelectedItem();
        try {
            Dialog editDialog = new Dialog();
            FXMLLoader loader = new FXMLLoader();
            Parent parent = loader.load(getClass().getResourceAsStream("/fxml/RatingEditPane.fxml"));
            ProjectGroupRatingEditPanePresenter presenter = loader.getController();
            presenter.setRating(selectedRating);
            presenter.setWindow(editDialog.getDialogPane().getScene().getWindow());
            editDialog.getDialogPane().setContent(parent);
            editDialog.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void removeRating() {
        Rating selectedRating = ratingsTableView.getSelectionModel().getSelectedItem();

        selectedRating.getParticipant().getRating().remove(selectedRating);
        participantDao.update(selectedRating.getParticipant());

        ratingDao.delete(selectedRating);
        loadRatingList();
    }

    @FXML
    public void generateReport() {
        ReportGenerator<String> reportGenerator = new SimpleTextProjectGroupRatingsSummaryReportGenerator(projectGroup);
        Report<String> report = reportGenerator.generate();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save report for " + projectGroup.getGroupName());
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Text files (*.txt)", "*.txt"));
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile == null) return;
        try {
            report.saveToFile(selectedFile.toPath());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Report saved to " + selectedFile.getAbsolutePath(), ButtonType.CLOSE);
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save report", ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    @Override
    public Optional<String> validateInput() {
        return Optional.empty();
    }

    @Override
    public void update() {
        return;
    }
}
