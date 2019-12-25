package pl.edu.agh.gastronomiastosowana.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import pl.edu.agh.gastronomiastosowana.dao.ProjectDao;
import pl.edu.agh.gastronomiastosowana.dao.ProjectGroupDao;
import pl.edu.agh.gastronomiastosowana.model.Project;
import pl.edu.agh.gastronomiastosowana.model.aggregations.ProjectList;
import pl.edu.agh.gastronomiastosowana.model.interactions.ItemInputType;
import pl.edu.agh.gastronomiastosowana.presenter.ProjectEditPanePresenter;

import java.io.IOException;
import java.time.LocalDate;

public class ProjectViewController {
    private ProjectList projectList;
    private ProjectDao projectDao;
    private ProjectGroupDao projectGroupDao;

    @FXML private TableView<Project> tableView;

    @FXML private Button addNewButton;
    @FXML private Button editButton;
    @FXML private Button removeButton;

    @FXML private Label projectGroupNameLabel;
    @FXML private Label activeLabel;
    @FXML private Label creationDateLabel;
    @FXML private Label leaderLabel;
    @FXML private Label participantCountLabel;
    @FXML private Label averageScoreLabel;

    @FXML
    private void initialize() {
        projectList = new ProjectList();
        projectDao = new ProjectDao();
        projectGroupDao = new ProjectGroupDao();

        bindTableProperties();
        bindButtonProperties();
        bindProjectGroupProperties();

        loadAll();
    }

    private void bindTableProperties() {
        tableView.itemsProperty().bind(projectList.getProperty());
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
    private void loadAll() {
        projectList.setElements(FXCollections.observableList(projectDao.findAll()));
    }

    @FXML
    private void loadActive() {
        projectList.setElements(FXCollections.observableList(projectDao.findActiveProjects()));
    }

    @FXML
    private void loadArchival() {
        projectList.setElements(FXCollections.observableList(projectDao.findArchivalProjects()));
    }

    @FXML
    private void loadFuture() {
        projectList.setElements(FXCollections.observableList(projectDao.findFutureProjects()));
    }

    @FXML
    void addNewProject() {
        try {
            Dialog editDialog = new Dialog();
            FXMLLoader loader = new FXMLLoader();
            Parent parent = loader.load(getClass().getResourceAsStream("/fxml/ProjectEditPane.fxml"));
            ProjectEditPanePresenter presenter = loader.getController();
            presenter.setItemInputType(ItemInputType.NEW_ITEM);
            presenter.setWindow(editDialog.getDialogPane().getScene().getWindow());
            editDialog.getDialogPane().setContent(parent);
            editDialog.showAndWait();

            if (presenter.isAccepted()) {
                if (presenter.getProject().getProjectGroup() != null) {
                    projectGroupDao.save(presenter.getProject().getProjectGroup());
                }
                projectDao.save(presenter.getProject());
                projectList.getElements().add(presenter.getProject());
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
            presenter.setItemInputType(ItemInputType.EDIT_ITEM);
            presenter.setProject(selectedProject);
            presenter.setWindow(editDialog.getDialogPane().getScene().getWindow());
            editDialog.getDialogPane().setContent(parent);
            editDialog.showAndWait();
            if (presenter.isAccepted()) {
                projectGroupDao.save(selectedProject.getProjectGroup());
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
        projectList.getElements().remove(selectedProject);
    }
}
