package pl.edu.agh.gastronomiastosowana.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import pl.edu.agh.gastronomiastosowana.dao.RatingDao;
import pl.edu.agh.gastronomiastosowana.model.Participant;
import pl.edu.agh.gastronomiastosowana.model.Rating;
import pl.edu.agh.gastronomiastosowana.model.exceptions.InvalidRatingValueException;
import pl.edu.agh.gastronomiastosowana.model.interactions.ItemInputType;

import java.time.LocalDate;
import java.util.Optional;

public class ParticipantEditPanePresenter {
    private RatingDao ratingDao;

    private Window window;
    private boolean accepted;
    private ItemInputType itemInputType;
    private Participant participant;

    @FXML private Label dialogTypeLabel;
    @FXML private TextField nameInput;
    @FXML private TextField surnameInput;
    @FXML private TextField ageInput;
    @FXML private TextField emailInput;

    @FXML private Label errorLabel;

    @FXML
    private void initialize() {
        window = null;
        accepted = false;
        setItemInputType(ItemInputType.NEW_ITEM);
        setParticipant(new Participant());

        ratingDao = new RatingDao();
    }

    @FXML
    private void accept() {
        Optional<String> error = validateInput();
        if (error.isPresent()) {
            errorLabel.setText(error.get());
            return;
        }

        updateParticipant();
        accepted = true;
        window.hide();
    }

    @FXML
    private void reject() {
        accepted = false;
        window.hide();
    }

    private Optional<String> validateInput() {
        final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        String name = Optional.ofNullable(nameInput.getText()).orElse("").trim();
        String surname = Optional.ofNullable(surnameInput.getText()).orElse("").trim();
        String age = Optional.ofNullable(ageInput.getText()).orElse("").trim();
        String email = Optional.ofNullable(emailInput.getText()).orElse("").trim();




        if (name.isEmpty())
            return Optional.of("Name cannot be empty");
        if (surname.isEmpty())
            return Optional.of("Surname cannot be empty");
        if (age.isEmpty())
            return Optional.of("Age cannot be empty");
        if (email.isEmpty())
            return Optional.of("Email cannot be empty");
        if (!email.matches(EMAIL_REGEX))
            return Optional.of("Email input invalid");

        return Optional.empty();
    }

    private void updateParticipant() {
        participant.setName(nameInput.getText().trim());
        participant.setSurname(surnameInput.getText().trim());
        participant.setAge(Integer.parseInt(ageInput.getText()));
        participant.setEmail(emailInput.getText());

    }

    public ItemInputType getItemInputType() {
        return itemInputType;
    }

    public void setItemInputType(ItemInputType itemInputType) {
        this.itemInputType = itemInputType;
        switch (this.itemInputType) {
            case NEW_ITEM:
                dialogTypeLabel.setText("Add new participant");
                break;
            case EDIT_ITEM:
                dialogTypeLabel.setText("Edit participant");
                break;
        }
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
        if(participant == null) {
            nameInput.clear();
            surnameInput.clear();
            ageInput.clear();
            emailInput.clear();
        }
        else {
            nameInput.setText(participant.getName());
            surnameInput.setText(participant.getSurname());
            ageInput.setText(Integer.toString(participant.getAge()));
            emailInput.setText(participant.getEmail());
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
