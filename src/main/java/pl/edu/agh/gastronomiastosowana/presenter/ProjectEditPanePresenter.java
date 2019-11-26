package pl.edu.agh.gastronomiastosowana.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import pl.edu.agh.gastronomiastosowana.model.Project;

import java.time.LocalDate;
import java.util.Optional;

public class ProjectEditPanePresenter {
    public enum InputType {
        NEW,
        EDIT
    }

    private Window window;
    private InputType inputType;
    private Project project;
    private boolean accepted;

    @FXML private Label dialogTypeLabel;
    @FXML private TextField projectNameInput;
    @FXML private DatePicker startDateInput;
    @FXML private DatePicker endDateInput;
    @FXML private Label projectGroupInput;
    @FXML private Label errorLabel;

    @FXML
    private void initialize() {
        accepted = false;
        setInputType(InputType.NEW);
        setProject(new Project());
    }

    @FXML
    private void accept(ActionEvent event) {
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
    private void reject(ActionEvent event) {
        accepted = false;
        window.hide();
    }

    private Optional<String> validateInput() {
        String name = projectNameInput.getText();
        if (name != null)
            name = name.trim();
        LocalDate startDate = startDateInput.getValue();
        LocalDate endDate = endDateInput.getValue();

        if (name == null || name.isEmpty())
            return Optional.of("Group name cannot be empty");
        if (startDate == null)
            return Optional.of("Start date cannot be empty");
        if (endDate != null && startDate.compareTo(endDate) > 0)
            return Optional.of("Start date is greater than end date");

        return Optional.empty();
    }

    private void updateProject() {
        project.setName(projectNameInput.getText());
        project.setStartDate(startDateInput.getValue());
        project.setEndDate(endDateInput.getValue());
    }

    @FXML
    void clearStartDateInput(ActionEvent event) {
        startDateInput.setValue(null);
    }

    @FXML
    void clearEndDateInput(ActionEvent event) {
        endDateInput.setValue(null);
    }

    public InputType getInputType() {
        return inputType;
    }

    public void setInputType(InputType inputType) {
        this.inputType = inputType;
        switch (this.inputType) {
            case NEW:
                dialogTypeLabel.setText("Create new project");
                break;
            case EDIT:
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
            projectGroupInput.setText("");
        }
        else {
            projectNameInput.setText(project.getName());
            startDateInput.setValue(project.getStartDate());
            endDateInput.setValue(project.getEndDate());
            if (project.getProjectGroup() != null)
                projectGroupInput.setText(project.getProjectGroup().getGroupName());
            else
                projectGroupInput.setText("");
        }
    }

    public void setWindow(Window window) {
        this.window = window;
        window.setOnCloseRequest(event -> reject(null));
    }

    public boolean isAccepted() {
        return accepted;
    }
}
