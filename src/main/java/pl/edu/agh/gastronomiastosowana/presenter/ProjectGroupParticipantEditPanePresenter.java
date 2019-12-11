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
import pl.edu.agh.gastronomiastosowana.model.exceptions.ChiefNotSetException;
import pl.edu.agh.gastronomiastosowana.model.exceptions.ChiefRemovalException;
import pl.edu.agh.gastronomiastosowana.model.exceptions.NonPresentParticipantRemovalException;
import pl.edu.agh.gastronomiastosowana.model.interactions.ItemInputType;

import java.time.LocalDate;
import java.util.Optional;
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
    @FXML private TextField projectNameInput;
    @FXML private DatePicker startDateInput;
    @FXML private DatePicker endDateInput;
    @FXML private TextField projectGroupInput;
    @FXML private Label projectGroupLabel;
    @FXML private Button projectGroupCancelButton;
    @FXML private Label errorLabel;
    @FXML private Button addButton;
    @FXML private Button removeButton;
    @FXML private Button setChiefButton;

    private void bindTableProperties() {
        tableCurrentUsersView.itemsProperty().bind(currentUsersList.participantsProperty());
        tableNotAssignedUsersView.itemsProperty().bind(notAssignedUsersList.participantsProperty());

    }
    private void bindButtonProperties() {
        BooleanBinding disableBindingActiveUsers = tableCurrentUsersView.getSelectionModel().selectedItemProperty().isNull();
        BooleanBinding disableBindingNonActiveUsers = tableNotAssignedUsersView.getSelectionModel().selectedItemProperty().isNull();

        addButton.disableProperty().bind(disableBindingNonActiveUsers);
        removeButton.disableProperty().bind(disableBindingActiveUsers);
        setChiefButton.disableProperty().bind(disableBindingActiveUsers);
    }
    @FXML
    private void initialize() {
        System.out.println("1");
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
    private void updateProjectGroup() {

    }
    @FXML
    private void accept() {
        Optional<String> error = validateInput();
        if (error.isPresent()) {
            errorLabel.setText(error.get());
            return;
        }

        updateProjectGroup();
        accepted = true;
        window.hide();
    }

    @FXML
    private void reject() {
        accepted = false;
        window.hide();
    }

    private Optional<String> validateInput() {
        String name = Optional.ofNullable(projectNameInput.getText()).orElse("").trim();
        LocalDate startDate = startDateInput.getValue();
        LocalDate endDate = endDateInput.getValue();

        if (name.isEmpty())
            return Optional.of("Project name cannot be empty");
        if (startDate == null)
            return Optional.of("Start date cannot be empty");
        if (endDate != null && startDate.compareTo(endDate) > 0)
            return Optional.of("Start date is greater than end date");
        return Optional.empty();
    }

    /*private void updateProject() {
        project.setName(projectNameInput.getText().trim());
        project.setStartDate(startDateInput.getValue());
        if (endDateInput.getValue() != null) {
            project.setEndDate(endDateInput.getValue());
        }
        if ( ! projectGroupInput.getText().equals("")) {
            project.setProjectGroup(projectGroupInput.getText().trim());
        }
    }*/
    @FXML
    private void loadCurrentParticipants() {
        System.out.println(projectGroup.getParticipants());
        currentUsersList.setParticipants(FXCollections.observableList(participantDao.
                findAll().
                stream().
                filter(x -> x.getWorksFor().contains(this.projectGroup)).
                collect(Collectors.toList())));
        System.out.println(currentUsersList);
    }
    @FXML
    private void loadNotAssignedParticipants() {

        notAssignedUsersList.setParticipants(FXCollections.observableList(participantDao.
                findAll().
                stream().
                filter(x -> ! x.getWorksFor().contains(this.projectGroup)).
                collect(Collectors.toList())));
        System.out.println(notAssignedUsersList);
    }
    @FXML
    void clearStartDateInput(ActionEvent event) {
        startDateInput.setValue(null);
    }

    @FXML
    void clearEndDateInput(ActionEvent event) {
        endDateInput.setValue(null);
    }

    public ItemInputType getItemInputType() {
        return itemInputType;
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
        } catch (NonPresentParticipantRemovalException e) {
            e.printStackTrace();
        } catch (ChiefRemovalException e) {
            e.printStackTrace();
        }

        tableNotAssignedUsersView.getItems().add(removedParticipant);
        tableCurrentUsersView.getItems().remove(removedParticipant);

        projectGroupDao.update(projectGroup);
    }

    public void addToGroup(ActionEvent actionEvent) {
        Participant addedParticipant = tableNotAssignedUsersView.getSelectionModel().getSelectedItem();
        projectGroup.addParticipant(addedParticipant);

        tableNotAssignedUsersView.getItems().remove(addedParticipant);
        tableCurrentUsersView.getItems().add(addedParticipant);

        projectGroupDao.update(projectGroup);
    }

    public void setAsChief(ActionEvent actionEvent) throws ChiefNotSetException {
        Participant newChief = tableCurrentUsersView.getSelectionModel().getSelectedItem();
        projectGroup.changeChief(newChief);

        projectGroupDao.update(projectGroup);

    }
}
