package pl.edu.agh.gastronomiastosowana.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;

import java.time.LocalDate;
import java.util.Optional;

public class ProjectGroupEditPanePresenter extends AbstractPresenter {

    private ProjectGroup projectGroup;
    @FXML
    private DatePicker creationDateInput;
    @FXML
    private CheckBox activeCheckBox;
    @FXML
    private TextField groupNameInput;

    @FXML
    private void initialize() {
        super.initialize("Project group");
        setProjectGroup(new ProjectGroup());
    }

    public Optional<String> validateInput() {
        String name = Optional.ofNullable(groupNameInput.getText()).orElse("").trim();
        LocalDate creationDate = creationDateInput.getValue();

        if (name.isEmpty())
            return Optional.of("Project name cannot be empty");
        if (creationDate == null)
            return Optional.of("Start date cannot be empty");

        return Optional.empty();
    }

    public void update() {
        projectGroup.setGroupName(groupNameInput.getText().trim());
        projectGroup.setCreationDate(creationDateInput.getValue());
        projectGroup.setActive(activeCheckBox.isSelected());
    }

    @FXML
    void clearCreationDateInput(ActionEvent event) {
        creationDateInput.setValue(null);
    }

    public ProjectGroup getProjectGroup() {
        return projectGroup;
    }

    public void setProjectGroup(ProjectGroup projectGroup) {

        this.projectGroup = projectGroup;
        if (projectGroup == null) {
            groupNameInput.clear();
            creationDateInput.setValue(null);
            activeCheckBox.setSelected(true);
        } else {
            groupNameInput.setText(projectGroup.getGroupName());
            creationDateInput.setValue(projectGroup.getCreationDate());
            activeCheckBox.setSelected(projectGroup.isActive());
        }
    }

}
