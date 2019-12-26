package pl.edu.agh.gastronomiastosowana.presenter;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pl.edu.agh.gastronomiastosowana.dao.RatingDao;
import pl.edu.agh.gastronomiastosowana.model.Participant;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;
import pl.edu.agh.gastronomiastosowana.model.Rating;
import pl.edu.agh.gastronomiastosowana.model.exceptions.InvalidRatingValueException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectGroupRatingPanePresenter extends AbstractPresenter {

    private ProjectGroup projectGroup;
    private List<Rating> newRatings = new ArrayList<>();
    private RatingDao ratingDao = new RatingDao();

    @FXML private RadioButton singleMemberRatingRadioButton;
    @FXML private ComboBox<String> participantNameBox;
    @FXML private TextField gradeTextField;
    @FXML private TextArea commentTextArea;

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
        var groupParticipants = projectGroup.getParticipants();
        var groupParticipantsNames = groupParticipants
                .stream()
                .map(Participant::getFullName)
                .collect(Collectors.toList());
        participantNameBox.setItems(FXCollections.observableList(groupParticipantsNames));
    }

    @Override
    public Optional<String> validateInput() {
        String participantName = Optional.ofNullable(participantNameBox.getValue()).orElse("");
        String comment = commentTextArea.getText();
        if(participantName.isEmpty() && singleMemberRatingRadioButton.isSelected()) {
            return Optional.of("No participant to rate was chosen");
        }
        try {
            double ratingValue = Double.parseDouble(gradeTextField.getText());
            if (singleMemberRatingRadioButton.isSelected()) {
                this.newRatings.add(new Rating(projectGroup.getParticipantByFullName(participantName), ratingValue, comment));
            } else {
                for (Participant participant : this.projectGroup.getParticipants()) {
                    this.newRatings.add(new Rating(participant, ratingValue, comment));
                }
            }
        } catch (NumberFormatException e) {
            return Optional.of("Incorrect number format");
        } catch (InvalidRatingValueException e) {
            return Optional.of("Wrong rating value");
        }
        return Optional.empty();
    }

    @Override
    public void update() {
        for (Rating rating : newRatings) {
            ratingDao.save(rating);
        }
    }
}
