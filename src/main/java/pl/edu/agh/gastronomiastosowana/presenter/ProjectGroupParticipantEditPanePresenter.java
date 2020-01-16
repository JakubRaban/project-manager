package pl.edu.agh.gastronomiastosowana.presenter;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import pl.edu.agh.gastronomiastosowana.dao.ParticipantDao;
import pl.edu.agh.gastronomiastosowana.dao.ProjectGroupDao;
import pl.edu.agh.gastronomiastosowana.model.Participant;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;
import pl.edu.agh.gastronomiastosowana.model.aggregations.ParticipantList;
import pl.edu.agh.gastronomiastosowana.model.exceptions.LeaderNotSetException;
import pl.edu.agh.gastronomiastosowana.model.exceptions.LeaderRemovalException;
import pl.edu.agh.gastronomiastosowana.model.exceptions.NonPresentParticipantRemovalException;
import pl.edu.agh.gastronomiastosowana.model.mail.MailMessage;
import pl.edu.agh.gastronomiastosowana.model.mail.SendMailService;

import java.util.Optional;

public class ProjectGroupParticipantEditPanePresenter extends AbstractPresenter {

    private ParticipantDao participantDao;
    private ProjectGroupDao projectGroupDao;
    private ParticipantList currentUsersList;
    private ParticipantList notAssignedUsersList;
    private SendMailService sendMailService;

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

        sendMailService = new SendMailService();

        bindButtonProperties();
    }

    @FXML
    private void loadCurrentParticipants() {
        currentUsersList.setElements(FXCollections.observableList(
                participantDao.findParticipantsAssignedTo(this.projectGroup)
        ));
    }
    @FXML
    private void loadNotAssignedParticipants() {
        notAssignedUsersList.setElements(FXCollections.observableList(
                participantDao.findParticipantsNotAssignedTo(this.projectGroup)
        ));
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
            sendParticipantRemovedMail(removedParticipant);
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
        sendParticipantAddedMail(addedParticipant);
        setErrorLabel(null);
    }

    public void setAsLeader(ActionEvent actionEvent) throws LeaderNotSetException {
        Participant newLeader = tableCurrentUsersView.getSelectionModel().getSelectedItem();
        projectGroup.setLeader(newLeader);

        projectGroupDao.update(projectGroup);
        loadCurrentLeader();
        setErrorLabel(null);
        sendLeaderSetMail(newLeader);
    }

    public Optional<String> validateInput() {
        return Optional.empty();
    }

    public void update() {
        return;
    }

    private void sendParticipantAddedMail(Participant participant) {
        String message = "Participant " + participant.getFullName() + " was added to group " + projectGroup.getGroupName();
        sendMailToAll(message);
    }

    private void sendLeaderSetMail(Participant leader) {
        String message = "Participant " + leader.getFullName() + " was made a leader of group " + projectGroup.getGroupName();
        sendMailToAll(message);
    }

    private void sendParticipantRemovedMail(Participant participant) {
        String message = "Participant " + participant.getFullName() + " was removed from group " + projectGroup.getGroupName();
        sendMailToAll(message);
        if (participant.isSubscribed())
            sendMailTo(participant, message);
    }

    private void sendMailToAll(String message) {
        for (Participant participant : projectGroup.getParticipants())
            if (participant.isSubscribed())
                sendMailTo(participant, message);
    }

    private void sendMailTo(Participant participant, String message) {
        MailMessage mail = new MailMessage();
        mail.setReceiver(participant.getEmail());
        mail.setSubject("Gastronomia update");
        mail.setText(message);
        sendMailService.sendEmail(mail);
    }
}
