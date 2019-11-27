package pl.edu.agh.gastronomiastosowana.controller;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import pl.edu.agh.gastronomiastosowana.dao.ParticipantDao;
import pl.edu.agh.gastronomiastosowana.model.Participant;
import pl.edu.agh.gastronomiastosowana.model.Project;
import pl.edu.agh.gastronomiastosowana.model.aggregations.ParticipantList;
import pl.edu.agh.gastronomiastosowana.model.interactions.ItemInputType;
import pl.edu.agh.gastronomiastosowana.presenter.ParticipantEditPanePresenter;
import pl.edu.agh.gastronomiastosowana.presenter.ProjectEditPanePresenter;

import java.io.IOException;

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
                participantList.getParticipants().add(presenter.getParticipant());
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
        participantList.getParticipants().remove(selectedParticipant);
    }
}
