package pl.edu.agh.gastronomiastosowana.presenter;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Window;
import pl.edu.agh.gastronomiastosowana.dao.ParticipantDao;
import pl.edu.agh.gastronomiastosowana.dao.ProjectGroupDao;
import pl.edu.agh.gastronomiastosowana.model.Participant;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;
import pl.edu.agh.gastronomiastosowana.model.aggregations.ParticipantList;
import pl.edu.agh.gastronomiastosowana.model.exceptions.LeaderNotSetException;
import pl.edu.agh.gastronomiastosowana.model.exceptions.LeaderRemovalException;
import pl.edu.agh.gastronomiastosowana.model.exceptions.NonPresentParticipantRemovalException;
import pl.edu.agh.gastronomiastosowana.model.interactions.ItemInputType;

import java.util.stream.Collectors;

public class ProjectGroupParticipantEditPanePresenter {

    private ParticipantDao participantDao;
    private ProjectGroupDao projectGroupDao;
    private ParticipantList currentUsersList;
    private ParticipantList notAssignedUsersList;

    @FXML
    private TableView<Participant> tableCurrentUsersView;
    @FXML
    private TableView<Participant> tableNotAssignedUsersView;

    private Window window;
    private boolean accepted;
    private ItemInputType itemInputType;
    private ProjectGroup projectGroup;

    @FXML private Label dialogTypeLabel;
    @FXML private Label currentLeaderLabel;
    @FXML private Label errorLabel;
    @FXML private Button addButton;
    @FXML private Button removeButton;
    @FXML private Button setLeaderButton;

    private void bindTableProperties() {
        tableCurrentUsersView.itemsProperty().bind(currentUsersList.getProperty());
        tableNotAssignedUsersView.itemsProperty().bind(notAssignedUsersList.getProperty());
    }
    private void bindButtonProperties() {
        // TODO: repair bindings
        BooleanBinding disableBindingActiveUsers = tableCurrentUsersView.getSelectionModel().selectedItemProperty().isNull();
        BooleanBinding disableBindingNonActiveUsers = tableNotAssignedUsersView.getSelectionModel().selectedItemProperty().isNull();

        addButton.disableProperty().bind(disableBindingNonActiveUsers);
        removeButton.disableProperty().bind(disableBindingActiveUsers);
        setLeaderButton.disableProperty().bind(disableBindingActiveUsers);
    }
    @FXML
    private void initialize() {
        window = null;
        accepted = false;
        setItemInputType(ItemInputType.NEW_ITEM);
        //setProjectGroup(new ProjectGroup());

        participantDao = new ParticipantDao();
        projectGroupDao = new ProjectGroupDao();

        currentUsersList = new ParticipantList();
        notAssignedUsersList = new ParticipantList();

        bindButtonProperties();
    }

    @FXML
    private void accept() {
        accepted = true;
        window.hide();
    }

    @FXML
    private void reject() {
        accepted = false;
        window.hide();
    }

    @FXML
    private void loadCurrentParticipants() {
        System.out.println(projectGroup.getParticipants());
        currentUsersList.setElements(FXCollections.observableList(participantDao.
                findAll().
                stream().
                filter(x -> x.getParticipatesIn().contains(this.projectGroup)).
                collect(Collectors.toList())));
    }
    @FXML
    private void loadNotAssignedParticipants() {

        notAssignedUsersList.setElements(FXCollections.observableList(participantDao.
                findAll().
                stream().
                filter(x -> ! x.getParticipatesIn().contains(this.projectGroup)).
                collect(Collectors.toList())));
    }
    @FXML
    private void loadCurrentLeader(){
        Participant leader = projectGroup.getLeader();
        if(leader != null)
            currentLeaderLabel.setText("Current leader: " + leader.getName()
                + " " + leader.getSurname());
        else
            currentLeaderLabel.setText("Current leader: none");
    }


    public void setItemInputType(ItemInputType itemInputType) {
        this.itemInputType = itemInputType;
        switch (this.itemInputType) {
            case NEW_ITEM:
                dialogTypeLabel.setText("Create new project");
                break;
            case EDIT_ITEM:
                dialogTypeLabel.setText("Edit project");
                break;
        }
    }

    public ProjectGroup getProjectGroup() {
        return projectGroup;
    }

    public void setProjectGroup(ProjectGroup projectGroup) {
        System.out.println("2");
        this.projectGroup = projectGroup;

        loadCurrentParticipants();
        loadNotAssignedParticipants();

        loadCurrentLeader();

        bindTableProperties();
        bindTableProperties();
    }

    public void setWindow(Window window) {
        this.window = window;
        window.setOnCloseRequest(event -> reject());
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void removeFromGroup(ActionEvent actionEvent) {
        Participant removedParticipant = tableCurrentUsersView.getSelectionModel().getSelectedItem();
        try {
            projectGroup.removeParticipant(removedParticipant);
            tableNotAssignedUsersView.getItems().add(removedParticipant);
            tableCurrentUsersView.getItems().remove(removedParticipant);
            errorLabel.setText(null);
        } catch (NonPresentParticipantRemovalException e) {
            e.printStackTrace();
        } catch (LeaderRemovalException e) {
            errorLabel.setText("Cannot remove group leader from group");
        }


        projectGroupDao.update(projectGroup);
    }

    public void addToGroup(ActionEvent actionEvent) {
        Participant addedParticipant = tableNotAssignedUsersView.getSelectionModel().getSelectedItem();
        projectGroup.addParticipant(addedParticipant);

        tableNotAssignedUsersView.getItems().remove(addedParticipant);
        tableCurrentUsersView.getItems().add(addedParticipant);

        projectGroupDao.update(projectGroup);
        errorLabel.setText(null);
    }

    public void setAsLeader(ActionEvent actionEvent) throws LeaderNotSetException {
        Participant newLeader = tableCurrentUsersView.getSelectionModel().getSelectedItem();
        projectGroup.setLeader(newLeader);

        projectGroupDao.update(projectGroup);
        loadCurrentLeader();
        errorLabel.setText(null);
    }
}
