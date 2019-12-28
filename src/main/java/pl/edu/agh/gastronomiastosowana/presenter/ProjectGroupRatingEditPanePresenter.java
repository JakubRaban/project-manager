package pl.edu.agh.gastronomiastosowana.presenter;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pl.edu.agh.gastronomiastosowana.dao.RatingDao;
import pl.edu.agh.gastronomiastosowana.model.Participant;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;
import pl.edu.agh.gastronomiastosowana.model.Rating;
import pl.edu.agh.gastronomiastosowana.model.RatingDetails;
import pl.edu.agh.gastronomiastosowana.model.exceptions.InvalidRatingValueException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectGroupRatingEditPanePresenter extends AbstractPresenter {

    private ProjectGroup projectGroup;
    private List<Rating> newRatings = new ArrayList<>();
    private RatingDao ratingDao = new RatingDao();

    @FXML private RadioButton singleMemberRatingRadioButton;
    @FXML private TextField titleTextField;
    @FXML private ComboBox<String> participantNameBox;
    @FXML private TextField gradeTextField;
    @FXML private TextField maxGradeTextField;
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
        groupParticipantsNames.add(0, "");
        participantNameBox.setItems(FXCollections.observableList(groupParticipantsNames));
    }

    @Override
    public Optional<String> validateInput() {
        String participantName = Optional.ofNullable(participantNameBox.getValue()).orElse("");
        String comment = Optional.ofNullable(commentTextArea.getText()).orElse("");
        String gradeText = gradeTextField.getText().replace(",", ".");
        String maxGradeText = maxGradeTextField.getText().replace(",", ".");
        String title = Optional.ofNullable(titleTextField.getText()).orElse("");
        if(participantName.isEmpty() && singleMemberRatingRadioButton.isSelected()) {
            return Optional.of("No participant to rate was chosen");
        }
        try {
            double ratingValue = Double.parseDouble(gradeText);
            double maxRatingValue = Double.parseDouble(maxGradeText);
            if (singleMemberRatingRadioButton.isSelected()) {
                var ratedParticipant = projectGroup.getParticipantByFullName(participantName);
                this.newRatings.add(new Rating(title, projectGroup, ratedParticipant, new RatingDetails(ratingValue, maxRatingValue), comment));
            } else {
                for (Participant participant : this.projectGroup.getParticipants()) {
                    this.newRatings.add(new Rating(title, projectGroup, participant, new RatingDetails(ratingValue, maxRatingValue), comment));
                }
            }
        } catch (NumberFormatException e) {
            if (gradeText.isEmpty()) return Optional.of("No grade was given");
            return Optional.of("Incorrect number format");
        } catch (InvalidRatingValueException e) {
            return Optional.of("Wrong rating value (rating smaller than 0 or greater than max)");
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
