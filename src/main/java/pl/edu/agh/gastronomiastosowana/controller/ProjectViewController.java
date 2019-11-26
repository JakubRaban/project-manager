package pl.edu.agh.gastronomiastosowana.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pl.edu.agh.gastronomiastosowana.dao.ProjectDao;
import pl.edu.agh.gastronomiastosowana.model.Project;
import pl.edu.agh.gastronomiastosowana.model.lists.ProjectList;

import java.time.LocalDate;

public class ProjectViewController {
    private ProjectList projectList;
    private ProjectDao projectDao;

    @FXML
    private TableView<Project> tableView;

    @FXML
    private Button addNewButton;
    @FXML
    private Button editButton;
    @FXML
    private Button removeButton;

    @FXML
    private Label projectGroupNameLabel;
    @FXML
    private Label activeLabel;
    @FXML
    private Label creationDateLabel;
    @FXML
    private Label chiefLabel;
    @FXML
    private Label participantCountLabel;
    @FXML
    private Label averageScoreLabel;

    @FXML
    private void initialize() {
        projectList = new ProjectList();
        projectDao = new ProjectDao();

        bindTableProperties();
        bindButtonProperties();
        bindProjectGroupProperties();
        loadAll(null);
    }

    private void bindTableProperties() {
        tableView.itemsProperty().bind(projectList.projectsProperty());
    }

    private void bindButtonProperties() {
        BooleanBinding disableBinding = tableView.getSelectionModel().selectedItemProperty().isNull();
        editButton.disableProperty().bind(disableBinding);
        removeButton.disableProperty().bind(disableBinding);
    }

    private void bindProjectGroupProperties() {
        ObjectBinding<String> groupNameBinding = Bindings.select(tableView.getSelectionModel().selectedItemProperty(), "projectGroup", "groupName");
        projectGroupNameLabel.textProperty().bind(groupNameBinding);

        ObjectBinding<Boolean> activeBinding = Bindings.select(tableView.getSelectionModel().selectedItemProperty(), "projectGroup", "active");
        StringBinding activeStringBinding = Bindings
                .when(activeBinding.isNotNull())
                .then(activeBinding.asString())
                .otherwise("");
        activeLabel.textProperty().bind(activeStringBinding);

        ObjectBinding<LocalDate> creationDateBinding = Bindings.select(tableView.getSelectionModel().selectedItemProperty(), "projectGroup", "creationDate");
        StringBinding creationDateStringBinding = Bindings
                .when(creationDateBinding.isNotNull())
                .then(creationDateBinding.asString())
                .otherwise("");
        creationDateLabel.textProperty().bind(creationDateStringBinding);
    }

    @FXML
    private void loadActive(ActionEvent event) {
        projectList.setProjects(FXCollections.observableArrayList());
    }

    @FXML
    private void loadAll(ActionEvent event) {
        projectList.setProjects(FXCollections.observableList(projectDao.findAll()));
    }

    @FXML
    private void loadArchival(ActionEvent event) {
        projectList.setProjects(FXCollections.observableArrayList());
    }

    @FXML
    void addNewProject() {
        Project selectedProject = tableView.getSelectionModel().getSelectedItem();

    }

    @FXML
    void editSelectedProject() {
        Project selectedProject = tableView.getSelectionModel().getSelectedItem();

    }

    @FXML
    void removeSelectedProject() {
        Project selectedProject = tableView.getSelectionModel().getSelectedItem();
        projectDao.delete(selectedProject);
        projectList.getProjects().remove(selectedProject);
    }
}
