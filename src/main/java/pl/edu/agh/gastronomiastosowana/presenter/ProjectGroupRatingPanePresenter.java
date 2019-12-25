package pl.edu.agh.gastronomiastosowana.presenter;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pl.edu.agh.gastronomiastosowana.model.Participant;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;

import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectGroupRatingPanePresenter extends AbstractPresenter {

    private ProjectGroup projectGroup;

    @FXML private RadioButton singleMemberRatingRadioButton;
    @FXML private RadioButton allMembersRatingRadioButton;
    @FXML private ComboBox<String> participantNameBox;
    @FXML private TextField gradeTextField;
    @FXML private TextArea commentTextArea;
    @FXML private Button okButton;
    @FXML private Button cancelButton;

    public void initialize() {
        super.initialize("Rating");
        setProjectGroup(new ProjectGroup());
        bindComboBox();
    }

    public void setProjectGroup(ProjectGroup projectGroup) {
        this.projectGroup = projectGroup;
        setParticipantsListItems();
    }

    private void bindComboBox() {
        BooleanBinding disableParticipantSelectionProperty = singleMemberRatingRadioButton.selectedProperty().not();
        participantNameBox.disableProperty().bind(disableParticipantSelectionProperty);
    }

    private void setParticipantsListItems() {
        var groupParticipantsNames = projectGroup.getParticipants()
                .stream()
                .map(Participant::getFullName)
                .collect(Collectors.toList());
        participantNameBox.setItems(FXCollections.observableList(groupParticipantsNames));
    }

    @Override
    public Optional<String> validateInput() {
        return Optional.of("Not implemented");
    }

    @Override
    public void update() {

    }
}
