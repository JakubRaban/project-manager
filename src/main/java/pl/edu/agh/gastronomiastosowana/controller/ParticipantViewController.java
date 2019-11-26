package pl.edu.agh.gastronomiastosowana.controller;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import pl.edu.agh.gastronomiastosowana.dao.ParticipantDao;
import pl.edu.agh.gastronomiastosowana.model.Participant;
import pl.edu.agh.gastronomiastosowana.model.aggregations.ParticipantList;

public class ParticipantViewController {
    private ParticipantList participantList;
    private ParticipantDao participantDao;

    @FXML private TableView<Participant> tableView;

    @FXML private Button addNewButton;
    @FXML private Button editButton;
    @FXML private Button removeButton;

    @FXML
    private void initialize() {
        participantList = new ParticipantList();
        participantDao = new ParticipantDao();

        bindTableProperties();
        bindButtonProperties();
        loadAll();
    }

    private void bindTableProperties() {
        tableView.itemsProperty().bind(participantList.participantsProperty());
    }

    private void bindButtonProperties() {
        BooleanBinding disableBinding = tableView.getSelectionModel().selectedItemProperty().isNull();
        editButton.disableProperty().bind(disableBinding);
        removeButton.disableProperty().bind(disableBinding);
    }

    @FXML
    private void loadAll() {
        participantList.setParticipants(FXCollections.observableList(participantDao.findAll()));
    }

    @FXML
    private void addNewParticipant() {

    }

    @FXML
    private void editSelectedParticipant() {

    }

    @FXML
    private void removeSelectedParticipant() {

    }
}
