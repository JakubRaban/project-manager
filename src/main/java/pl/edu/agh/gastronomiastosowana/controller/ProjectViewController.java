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
import pl.edu.agh.gastronomiastosowana.dao.ProjectDao;
import pl.edu.agh.gastronomiastosowana.model.Project;
import pl.edu.agh.gastronomiastosowana.model.lists.ProjectList;
import pl.edu.agh.gastronomiastosowana.presenter.ProjectEditPanePresenter;

import java.io.IOException;
import java.time.LocalDate;

public class ProjectViewController {
    private ProjectList projectList;
    private ProjectDao projectDao;

    @FXML private TableView<Project> tableView;

    @FXML private Button addNewButton;
    @FXML private Button editButton;
    @FXML private Button removeButton;

    @FXML private Label projectGroupNameLabel;
    @FXML private Label activeLabel;
    @FXML private Label creationDateLabel;
    @FXML private Label chiefLabel;
    @FXML private Label participantCountLabel;
    @FXML private Label averageScoreLabel;

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
        // TODO: fix warnings
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
        try {
            Dialog editDialog = new Dialog();
            FXMLLoader loader = new FXMLLoader();
            Parent parent = loader.load(getClass().getResourceAsStream("/fxml/ProjectEditPane.fxml"));
            ProjectEditPanePresenter presenter = loader.getController();
            presenter.setInputType(ProjectEditPanePresenter.InputType.NEW);
            presenter.setWindow(editDialog.getDialogPane().getScene().getWindow());
            editDialog.getDialogPane().setContent(parent);
            editDialog.showAndWait();

            if (presenter.isAccepted()) {
                projectDao.save(presenter.getProject());
                projectList.getProjects().add(presenter.getProject());
            }
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @FXML
    void editSelectedProject() {
        Project selectedProject = tableView.getSelectionModel().getSelectedItem();
        try {
            Dialog editDialog = new Dialog();
            FXMLLoader loader = new FXMLLoader();
            Parent parent = loader.load(getClass().getResourceAsStream("/fxml/ProjectEditPane.fxml"));
            ProjectEditPanePresenter presenter = loader.getController();
            presenter.setInputType(ProjectEditPanePresenter.InputType.EDIT);
            presenter.setProject(selectedProject);
            presenter.setWindow(editDialog.getDialogPane().getScene().getWindow());
            editDialog.getDialogPane().setContent(parent);
            editDialog.showAndWait();
            if (presenter.isAccepted()) {
                projectDao.save(selectedProject);
            }
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @FXML
    void removeSelectedProject() {
        Project selectedProject = tableView.getSelectionModel().getSelectedItem();
        projectDao.delete(selectedProject);
        projectList.getProjects().remove(selectedProject);
    }
}
