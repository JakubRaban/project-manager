package pl.edu.agh.gastronomiastosowana.controller;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import pl.edu.agh.gastronomiastosowana.dao.ParticipantDao;
import pl.edu.agh.gastronomiastosowana.model.Participant;
import pl.edu.agh.gastronomiastosowana.model.aggregations.ParticipantList;
import pl.edu.agh.gastronomiastosowana.model.importer.AbstractCsvImporter;
import pl.edu.agh.gastronomiastosowana.model.importer.ImportResult;
import pl.edu.agh.gastronomiastosowana.model.importer.ParticipantCsvImporter;
import pl.edu.agh.gastronomiastosowana.model.interactions.ItemInputType;
import pl.edu.agh.gastronomiastosowana.presenter.ParticipantEditPanePresenter;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ParticipantViewController {
    private ParticipantList participantList;
    private ParticipantDao participantDao;

    @FXML private TableView<Participant> tableView;

    @FXML private Button addNewButton;
    @FXML private Button editButton;
    @FXML private Button removeButton;
    @FXML private Button importButton;

    @FXML
    private void initialize() {
        participantList = new ParticipantList();
        participantDao = new ParticipantDao();

        bindTableProperties();
        bindButtonProperties();
        loadAll();
    }

    private void bindTableProperties() {
        tableView.itemsProperty().bind(participantList.getProperty());
    }

    private void bindButtonProperties() {
        BooleanBinding disableBinding = tableView.getSelectionModel().selectedItemProperty().isNull();
        editButton.disableProperty().bind(disableBinding);
        removeButton.disableProperty().bind(disableBinding);
    }

    @FXML
    private void loadAll() {
        participantList.setElements(FXCollections.observableList(participantDao.findAll()));
    }

    @FXML
    private void addNewParticipant() {
        try {
            Dialog editDialog = new Dialog();
            FXMLLoader loader = new FXMLLoader();
            Parent parent = loader.load(getClass().getResourceAsStream("/fxml/ParticipantEditPane.fxml"));
            ParticipantEditPanePresenter presenter = loader.getController();
            presenter.setItemInputType(ItemInputType.NEW_ITEM);
            presenter.setWindow(editDialog.getDialogPane().getScene().getWindow());
            editDialog.getDialogPane().setContent(parent);
            editDialog.showAndWait();

            if (presenter.isAccepted()) {
                participantDao.save(presenter.getParticipant());
                participantList.getElements().add(presenter.getParticipant());
            }
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @FXML
    private void editSelectedParticipant() {
        Participant selectedParticipant = tableView.getSelectionModel().getSelectedItem();
        try {
            Dialog editDialog = new Dialog();
            FXMLLoader loader = new FXMLLoader();
            Parent parent = loader.load(getClass().getResourceAsStream("/fxml/ParticipantEditPane.fxml"));
            ParticipantEditPanePresenter presenter = loader.getController();
            presenter.setItemInputType(ItemInputType.EDIT_ITEM);
            presenter.setParticipant(selectedParticipant);
            presenter.setWindow(editDialog.getDialogPane().getScene().getWindow());
            editDialog.getDialogPane().setContent(parent);
            editDialog.showAndWait();
            if (presenter.isAccepted()) {
                participantDao.save(selectedParticipant);
            }
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @FXML
    private void removeSelectedParticipant() {
        Participant selectedParticipant = tableView.getSelectionModel().getSelectedItem();
        participantDao.delete(selectedParticipant);
        participantList.getElements().remove(selectedParticipant);
    }

    @FXML
    private void importFromCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file to import");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV files (*.csv)", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile == null) return;
        try {
            AbstractCsvImporter<Participant> importer = new ParticipantCsvImporter(selectedFile.toPath());
            ImportResult<Participant> importResult = importer.doImport();
            List<Participant> importedEntities = importResult.getImportedEntities();
            if (importedEntities.size() > 0) {
                participantDao.save(importedEntities.toArray(Participant[]::new));
            }
            new Alert(AlertType.INFORMATION, "Successfully imported " + importResult.getSuccessfullyImported()
            + " entities. \n" + "Failed to import " + importResult.getNotImported() + " entities.").showAndWait();
            loadAll();
        } catch (IOException e) {
            new Alert(AlertType.ERROR, "Could not open specified file", ButtonType.CLOSE).showAndWait();
        }
    }
}
