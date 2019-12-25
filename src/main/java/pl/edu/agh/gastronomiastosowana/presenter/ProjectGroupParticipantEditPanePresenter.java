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

import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectGroupParticipantEditPanePresenter extends AbstractPresenter {

    private ParticipantDao participantDao;
    private ProjectGroupDao projectGroupDao;
    private ParticipantList currentUsersList;
    private ParticipantList notAssignedUsersList;

    @FXML
    private TableView<Participant> tableCurrentUsersView;
    @FXML
    private TableView<Participant> tableNotAssignedUsersView;

    private ProjectGroup projectGroup;

    @FXML private Label currentLeaderLabel;
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
    public void initialize() {
        super.initialize("Participants in Project group");

        participantDao = new ParticipantDao();
        projectGroupDao = new ProjectGroupDao();

        currentUsersList = new ParticipantList();
        notAssignedUsersList = new ParticipantList();

        bindButtonProperties();
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

    public ProjectGroup getProjectGroup() {
        return projectGroup;
    }

    public void setProjectGroup(ProjectGroup projectGroup) {
        this.projectGroup = projectGroup;

        loadCurrentParticipants();
        loadNotAssignedParticipants();

        loadCurrentLeader();

        bindTableProperties();
        bindTableProperties();
    }

    public void removeFromGroup(ActionEvent actionEvent) {
        Participant removedParticipant = tableCurrentUsersView.getSelectionModel().getSelectedItem();
        try {
            projectGroup.removeParticipant(removedParticipant);
            tableNotAssignedUsersView.getItems().add(removedParticipant);
            tableCurrentUsersView.getItems().remove(removedParticipant);
            setErrorLabel(null);
        } catch (NonPresentParticipantRemovalException e) {
            e.printStackTrace();
        } catch (LeaderRemovalException e) {
            setErrorLabel("Cannot remove group leader from group");
        }


        projectGroupDao.update(projectGroup);
    }

    public void addToGroup(ActionEvent actionEvent) {
        Participant addedParticipant = tableNotAssignedUsersView.getSelectionModel().getSelectedItem();
        projectGroup.addParticipant(addedParticipant);

        tableNotAssignedUsersView.getItems().remove(addedParticipant);
        tableCurrentUsersView.getItems().add(addedParticipant);

        projectGroupDao.update(projectGroup);
        setErrorLabel(null);
    }

    public void setAsLeader(ActionEvent actionEvent) throws LeaderNotSetException {
        Participant newLeader = tableCurrentUsersView.getSelectionModel().getSelectedItem();
        projectGroup.setLeader(newLeader);

        projectGroupDao.update(projectGroup);
        loadCurrentLeader();
        setErrorLabel(null);
    }

    public Optional<String> validateInput() {
        return Optional.empty();
    }

    public void update() {
        return;
    }
}
