package pl.edu.agh.gastronomiastosowana.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Window;
import pl.edu.agh.gastronomiastosowana.dao.ParticipantDao;
import pl.edu.agh.gastronomiastosowana.model.Participant;
import pl.edu.agh.gastronomiastosowana.model.Project;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;
import pl.edu.agh.gastronomiastosowana.model.aggregations.ParticipantList;
import pl.edu.agh.gastronomiastosowana.model.exceptions.GroupAlreadyAssignedException;
import pl.edu.agh.gastronomiastosowana.model.interactions.ItemInputType;

import java.time.LocalDate;
import java.util.Optional;

public class ProjectGroupEditPanePresenter {
    private ParticipantDao participantDao;
    private ParticipantList participantList;

    @FXML
    private TableView<Participant> tableCurrentUsersView;
    private Window window;
    private boolean accepted;
    private ItemInputType itemInputType;
    private ProjectGroup projectGroup;

    @FXML private Label dialogTypeLabel;
    @FXML private TextField projectNameInput;
    @FXML
    private DatePicker startDateInput;
    @FXML private DatePicker endDateInput;
    @FXML private TextField projectGroupInput;
    @FXML private Label projectGroupLabel;
    @FXML private Button projectGroupCancelButton;
    @FXML private Label errorLabel;

    @FXML
    private void initialize() {
        participantDao = new ParticipantDao();
        participantList = new ParticipantList();

        window = null;
        accepted = false;
        setItemInputType(ItemInputType.NEW_ITEM);
        setProjectGroup(new ProjectGroup());

        bindTableProperties();
    }
    private void bindTableProperties() {
        tableCurrentUsersView.itemsProperty().bind(participantList.participantsProperty());
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
        this.projectGroup = projectGroup;
    }

    public void setWindow(Window window) {
        this.window = window;
        window.setOnCloseRequest(event -> reject());
    }

    public boolean isAccepted() {
        return accepted;
    }

}
