package pl.edu.agh.gastronomiastosowana.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Window;
import pl.edu.agh.gastronomiastosowana.dao.ParticipantDao;
import pl.edu.agh.gastronomiastosowana.dao.ProjectGroupDao;
import pl.edu.agh.gastronomiastosowana.model.Participant;
import pl.edu.agh.gastronomiastosowana.model.Project;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;
import pl.edu.agh.gastronomiastosowana.model.aggregations.ParticipantList;
import pl.edu.agh.gastronomiastosowana.model.aggregations.ProjectGroupList;
import pl.edu.agh.gastronomiastosowana.model.exceptions.GroupAlreadyAssignedException;
import pl.edu.agh.gastronomiastosowana.model.interactions.ItemInputType;

import java.time.LocalDate;
import java.util.Optional;

public class ProjectGroupEditPanePresenter {

    @FXML
    private Window window;
    private boolean accepted;
    private ItemInputType itemInputType;
    private ProjectGroup projectGroup;

    @FXML private Label dialogTypeLabel;
    @FXML
    private DatePicker creationDateInput;

    @FXML private CheckBox activeCheckBox;
    @FXML private TextField groupNameInput;
    @FXML private Label errorLabel;

    @FXML
    private void initialize() {

        window = null;
        accepted = false;
        setItemInputType(ItemInputType.NEW_ITEM);
        setProjectGroup(new ProjectGroup());

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
        String name = Optional.ofNullable(groupNameInput.getText()).orElse("").trim();
        LocalDate creationDate = creationDateInput.getValue();

        if (name.isEmpty())
            return Optional.of("Project name cannot be empty");
        if (creationDate == null)
            return Optional.of("Start date cannot be empty");

        return Optional.empty();
    }

    private void updateProjectGroup() {
        projectGroup.setGroupName(groupNameInput.getText().trim());
        projectGroup.setCreationDate(creationDateInput.getValue());
        projectGroup.setActive(activeCheckBox.isSelected());
    }

    @FXML
    void clearCreationDateInput(ActionEvent event) {
        creationDateInput.setValue(null);
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
        if(projectGroup == null){
            groupNameInput.clear();
            creationDateInput.setValue(null);
            activeCheckBox.setSelected(true);
        }
        else{
            groupNameInput.setText(projectGroup.getGroupName());
            creationDateInput.setValue(projectGroup.getCreationDate());
            activeCheckBox.setSelected(projectGroup.isActive());
        }
    }

    public void setWindow(Window window) {
        this.window = window;
        window.setOnCloseRequest(event -> reject());
    }

    public boolean isAccepted() {
        return accepted;
    }

}
