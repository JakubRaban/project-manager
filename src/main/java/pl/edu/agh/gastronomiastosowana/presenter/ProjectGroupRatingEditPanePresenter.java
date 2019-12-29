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
import pl.edu.agh.gastronomiastosowana.model.interactions.ItemInputType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectGroupRatingEditPanePresenter extends AbstractPresenter {

    private ProjectGroup projectGroup;
    private Rating rating;
    private List<Rating> newRatings = new ArrayList<>();
    private RatingDao ratingDao = new RatingDao();

    @FXML private RadioButton singleMemberRatingRadioButton;
    @FXML private RadioButton allMembersRatingRadioButton;
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

    public void setRating(Rating rating) {
        this.rating = rating;
        if(rating != null) {
            this.setItemInputType(ItemInputType.EDIT_ITEM);
            this.singleMemberRatingRadioButton.setSelected(true);
            this.allMembersRatingRadioButton.setDisable(true);
            this.titleTextField.setText(rating.getRatingTitle());
            this.participantNameBox.setValue(rating.getParticipant().getFullName());
            this.participantNameBox.disableProperty().unbind();
            this.participantNameBox.setDisable(true);
            this.gradeTextField.setText(Double.toString(rating.getRatingDetails().getRatingValue()));
            this.maxGradeTextField.setText(Double.toString(rating.getRatingDetails().getMaxRatingValue()));
            this.commentTextArea.setText(rating.getComment());
        }
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
            if (getItemInputType() == ItemInputType.NEW_ITEM) {
                if (singleMemberRatingRadioButton.isSelected()) {
                    var ratedParticipant = projectGroup.getParticipantByFullName(participantName);
                    this.newRatings.add(new Rating(title, projectGroup, ratedParticipant, new RatingDetails(ratingValue, maxRatingValue), comment));
                } else {
                    for (Participant participant : this.projectGroup.getParticipants()) {
                        this.newRatings.add(new Rating(title, projectGroup, participant, new RatingDetails(ratingValue, maxRatingValue), comment));
                    }
                }
            } else {
                rating.setRatingTitle(title);
                rating.setRatingDetails(new RatingDetails(ratingValue, maxRatingValue));
                rating.setComment(comment);
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
        if (getItemInputType() == ItemInputType.NEW_ITEM) {
            ratingDao.save(newRatings.toArray(new Rating[0]));
        } else {
            ratingDao.update(rating);
        }
    }
}
