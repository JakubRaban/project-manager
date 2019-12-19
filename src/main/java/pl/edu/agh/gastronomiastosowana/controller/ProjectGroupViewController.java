package pl.edu.agh.gastronomiastosowana.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import pl.edu.agh.gastronomiastosowana.dao.ParticipantDao;
import pl.edu.agh.gastronomiastosowana.dao.ProjectDao;
import pl.edu.agh.gastronomiastosowana.dao.ProjectGroupDao;
import pl.edu.agh.gastronomiastosowana.model.Participant;
import pl.edu.agh.gastronomiastosowana.model.Project;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;
import pl.edu.agh.gastronomiastosowana.model.aggregations.ProjectGroupList;
import pl.edu.agh.gastronomiastosowana.model.aggregations.ProjectList;
import pl.edu.agh.gastronomiastosowana.model.interactions.ItemInputType;
import pl.edu.agh.gastronomiastosowana.presenter.ProjectEditPanePresenter;
import pl.edu.agh.gastronomiastosowana.presenter.ProjectGroupEditPanePresenter;
import pl.edu.agh.gastronomiastosowana.presenter.ProjectGroupParticipantEditPanePresenter;

import java.io.IOException;
import java.time.LocalDate;

public class ProjectGroupViewController {

    private ProjectGroupList projectGroupList;
    private ProjectDao projectDao;
    private ProjectGroupDao projectGroupDao;
    private ParticipantDao participantDao;

    @FXML private TableView<ProjectGroup> tableView;

    @FXML private Label chiefLabel;
    @FXML private Label creationDateLabel;
    @FXML private Label activeLabel;
    @FXML private Label projectNameLabel;
    @FXML private Button editButton;
    @FXML private Button editParticipantsButton;

    @FXML
    private void initialize() {
        projectGroupList = new ProjectGroupList();
        projectDao = new ProjectDao();
        projectGroupDao = new ProjectGroupDao();

        bindTableProperties();
        bindButtonProperties();
        bindProjectProperties();
        loadAll();
    }
    private void bindTableProperties() {
        tableView.itemsProperty().bind(projectGroupList.projectGroupProperty());
    }

    private void bindButtonProperties() {
        BooleanBinding disableBinding = tableView.getSelectionModel().selectedItemProperty().isNull();
        editButton.disableProperty().bind(disableBinding);
        editParticipantsButton.disableProperty().bind(disableBinding);
    }

    private void bindProjectProperties() {
        // TODO: fix warnings
        ObjectBinding<String> projectNameBinding = Bindings.select(tableView.getSelectionModel().selectedItemProperty(), "project", "name");
        projectNameLabel.textProperty().bind(projectNameBinding);

        ObjectBinding<Boolean> activeBinding = Bindings.select(tableView.getSelectionModel().selectedItemProperty(), "active");
        StringBinding activeStringBinding = Bindings
                .when(activeBinding.isNotNull())
                .then(activeBinding.asString())
                .otherwise("");
        activeLabel.textProperty().bind(activeStringBinding);


        ObjectBinding<LocalDate> creationDateBinding = Bindings.select(tableView.getSelectionModel().selectedItemProperty(), "creationDate");
        StringBinding creationDateStringBinding = Bindings
                .when(creationDateBinding.isNotNull())
                .then(creationDateBinding.asString())
                .otherwise("");
        creationDateLabel.textProperty().bind(creationDateStringBinding);

        ObjectBinding<LocalDate> chiefBinding = Bindings.select(tableView.getSelectionModel().selectedItemProperty(), "chief", "name");
        StringBinding chiefStringBinding = Bindings
                .when(chiefBinding.isNotNull())
                .then(chiefBinding.asString())
                .otherwise("");
        chiefLabel.textProperty().bind(chiefStringBinding);
    }

    @FXML
    private void loadActive() {
        projectGroupList.setProjectGroups(FXCollections.observableArrayList());
    }

    @FXML
    private void loadAll() {
        projectGroupList.setProjectGroups((FXCollections.observableList(projectGroupDao.findAll())));
    }

    @FXML
    private void loadArchival() {
        projectGroupList.setProjectGroups(FXCollections.observableArrayList());
    }

    @FXML
    void editSelectedProject() {
        ProjectGroup selectedProjectGroup = tableView.getSelectionModel().getSelectedItem();
        try {
            Dialog editDialog = new Dialog();
            FXMLLoader loader = new FXMLLoader();
            Parent parent = loader.load(getClass().getResourceAsStream("/fxml/ProjectGroupEditPane.fxml"));
            ProjectGroupEditPanePresenter presenter = loader.getController();
            presenter.setItemInputType(ItemInputType.EDIT_ITEM);
            presenter.setProjectGroup(selectedProjectGroup);
            presenter.setWindow(editDialog.getDialogPane().getScene().getWindow());
            editDialog.getDialogPane().setContent(parent);
            editDialog.showAndWait();
            if (presenter.isAccepted()) {
                projectGroupDao.save(selectedProjectGroup);
                //projectDao.save(selectedProjectGroup);
            }
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    public void editParticipants(ActionEvent actionEvent) {
        ProjectGroup selectedProjectGroup = tableView.getSelectionModel().getSelectedItem();
        try {
            Dialog editDialog = new Dialog();
            FXMLLoader loader = new FXMLLoader();
            Parent parent = loader.load(getClass().getResourceAsStream("/fxml/ProjectGroupParticipantsEditPane.fxml"));
            ProjectGroupParticipantEditPanePresenter presenter = loader.getController();
            presenter.setItemInputType(ItemInputType.EDIT_ITEM);
            presenter.setProjectGroup(selectedProjectGroup);
            presenter.setWindow(editDialog.getDialogPane().getScene().getWindow());
            editDialog.getDialogPane().setContent(parent);
            editDialog.showAndWait();
            if (presenter.isAccepted()) {
                projectGroupDao.save(selectedProjectGroup);
                //projectDao.save(selectedProjectGroup);
            }
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }
}