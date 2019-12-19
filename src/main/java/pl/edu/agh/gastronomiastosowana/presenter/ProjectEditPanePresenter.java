package pl.edu.agh.gastronomiastosowana.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import pl.edu.agh.gastronomiastosowana.model.Project;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;
import pl.edu.agh.gastronomiastosowana.model.exceptions.GroupAlreadyAssignedException;
import pl.edu.agh.gastronomiastosowana.model.interactions.ItemInputType;

import java.time.LocalDate;
import java.util.Optional;

public class ProjectEditPanePresenter {
    private Window window;
    private boolean accepted;
    private ItemInputType itemInputType;
    private Project project;

    @FXML private Label dialogTypeLabel;
    @FXML private TextField projectNameInput;
    @FXML private DatePicker startDateInput;
    @FXML private DatePicker endDateInput;
    @FXML private TextField projectGroupInput;
    @FXML private Label projectGroupLabel;
    @FXML private Button projectGroupCancelButton;
    @FXML private Label errorLabel;

    @FXML
    private void initialize() {
        window = null;
        accepted = false;
        setItemInputType(ItemInputType.NEW_ITEM);
        setProject(new Project());
    }

    @FXML
    private void accept() {
        Optional<String> error = validateInput();
        if (error.isPresent()) {
            errorLabel.setText(error.get());
            return;
        }

        updateProject();
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

    private void updateProject() {
        project.setName(projectNameInput.getText().trim());
        project.setStartDate(startDateInput.getValue());
        if (endDateInput.getValue() != null) {
            project.setEndDate(endDateInput.getValue());
        }
        if ( ! projectGroupInput.getText().equals("")) {
            project.setProjectGroup(projectGroupInput.getText().trim());
        }
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
        if (project == null) {
            projectNameInput.clear();
            startDateInput.setValue(null);
            endDateInput.setValue(null);
            projectGroupInput.clear();
        }
        else {
            projectNameInput.setText(project.getName());
            startDateInput.setValue(project.getStartDate());
            endDateInput.setValue(project.getEndDate());
            if (project.getProjectGroup() != null) {
                projectGroupInput.setVisible(false);

                projectGroupLabel.setVisible(true);
                projectGroupLabel.setText(project.getProjectGroup().getGroupName());

                projectGroupCancelButton.setVisible(true);

                //projectGroupInput.setText(project.getProjectGroup().getGroupName());
            }
            else {
                projectGroupInput.setVisible(true);
                projectGroupLabel.setVisible(false);
                projectGroupCancelButton.setVisible(false);

                projectGroupInput.setText("");
            }
            //if project has project group assigned - show project group label
            //else show text box with project group input


        }
    }

    public void setWindow(Window window) {
        this.window = window;
        window.setOnCloseRequest(event -> reject());
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void cancelGroupAssignment(){
        project.cancelProjectGroup();

        projectGroupInput.setVisible(true);
        projectGroupLabel.setVisible(false);
        projectGroupCancelButton.setVisible(false);
    }
}
