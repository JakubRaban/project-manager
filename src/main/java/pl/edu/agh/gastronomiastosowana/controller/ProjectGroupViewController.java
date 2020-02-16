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
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import pl.edu.agh.gastronomiastosowana.dao.ParticipantDao;
import pl.edu.agh.gastronomiastosowana.dao.ProjectDao;
import pl.edu.agh.gastronomiastosowana.dao.ProjectGroupDao;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;
import pl.edu.agh.gastronomiastosowana.model.aggregations.ProjectGroupList;
import pl.edu.agh.gastronomiastosowana.model.interactions.ItemInputType;
import pl.edu.agh.gastronomiastosowana.presenter.ProjectGroupEditPanePresenter;
import pl.edu.agh.gastronomiastosowana.presenter.ProjectGroupParticipantEditPanePresenter;
import pl.edu.agh.gastronomiastosowana.presenter.ProjectGroupRatingEditPanePresenter;
import pl.edu.agh.gastronomiastosowana.presenter.ProjectGroupRatingViewPanePresenter;

import java.io.IOException;
import java.time.LocalDate;

public class ProjectGroupViewController {

    private ProjectGroupList projectGroupList;
    private ProjectDao projectDao;
    private ProjectGroupDao projectGroupDao;
    private ParticipantDao participantDao;

    @FXML private TableView<ProjectGroup> tableView;

    @FXML private Label leaderLabel;
    @FXML private Label creationDateLabel;
    @FXML private Label activeLabel;
    @FXML private Label projectNameLabel;
    @FXML private Label participantCountLabel;
    @FXML private Button editButton;
    @FXML private Button editParticipantsButton;
    @FXML private Button addGradeButton;
    @FXML private Button viewGradeButton;

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
        tableView.itemsProperty().bind(projectGroupList.getProperty());
    }

    private void bindButtonProperties() {
        BooleanBinding disableBinding = tableView.getSelectionModel().selectedItemProperty().isNull();
        editButton.disableProperty().bind(disableBinding);
        editParticipantsButton.disableProperty().bind(disableBinding);
        addGradeButton.disableProperty().bind(disableBinding);
        viewGradeButton.disableProperty().bind(disableBinding);
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

        ObjectBinding<String> leaderBinding = Bindings.select(tableView.getSelectionModel().selectedItemProperty(), "leader", "nameEmailLabel");
        StringBinding leaderStringBinding = Bindings
                .when(leaderBinding.isNotNull())
                .then(leaderBinding.asString())
                .otherwise("");
        leaderLabel.textProperty().bind(leaderStringBinding);

        ObjectBinding<Integer> participantCountBinding = Bindings.select(tableView.getSelectionModel().selectedItemProperty(), "participantCount");
        StringBinding participantCountStringBinding = Bindings
                .when(participantCountBinding.isNotNull())
                .then(participantCountBinding.asString())
                .otherwise("");
        participantCountLabel.textProperty().bind(participantCountStringBinding);
    }

    @FXML
    private void loadActive() {
        projectGroupList.setElements(FXCollections.observableList(projectGroupDao.findActiveProjectGroups()));
    }

    @FXML
    private void loadAll() {
        projectGroupList.setElements((FXCollections.observableList(projectGroupDao.findAll())));
    }

    @FXML
    private void loadArchival() {
        projectGroupList.setElements(FXCollections.observableList(projectGroupDao.findArchivalProjectGroups()));
    }

    @FXML
    private void loadFuture() {
        projectGroupList.setElements(FXCollections.observableList(projectGroupDao.findFutureProjectGroups()));
    }

    @FXML
    void editSelectedProjectGroup() {
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

    public void editParticipants() {
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

    @FXML
    public void rateProjectParticipant() {
        ProjectGroup selectedGroup = tableView.getSelectionModel().getSelectedItem();
        try {
            Dialog editDialog = new Dialog();
            FXMLLoader loader = new FXMLLoader();
            Parent parent = loader.load(getClass().getResourceAsStream("/fxml/RatingEditPane.fxml"));
            ProjectGroupRatingEditPanePresenter presenter = loader.getController();
            presenter.setItemInputType(ItemInputType.NEW_ITEM);
            presenter.setProjectGroup(selectedGroup);
            presenter.setWindow(editDialog.getDialogPane().getScene().getWindow());
            editDialog.getDialogPane().setContent(parent);
            editDialog.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void viewRatings() {
        ProjectGroup selectedGroup = tableView.getSelectionModel().getSelectedItem();
        try {
            Dialog editDialog = new Dialog();
            FXMLLoader loader = new FXMLLoader();
            Parent parent = loader.load(getClass().getResourceAsStream("/fxml/RatingViewPane.fxml"));
            ProjectGroupRatingViewPanePresenter presenter = loader.getController();
            presenter.setProjectGroup(selectedGroup);
            presenter.setWindow(editDialog.getDialogPane().getScene().getWindow());
            editDialog.getDialogPane().setContent(parent);
            editDialog.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
