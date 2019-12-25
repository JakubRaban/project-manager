package pl.edu.agh.gastronomiastosowana.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import pl.edu.agh.gastronomiastosowana.dao.RatingDao;
import pl.edu.agh.gastronomiastosowana.model.Participant;

import java.util.Optional;

public class ParticipantEditPanePresenter extends AbstractPresenter {
    private Participant participant;
    private RatingDao ratingDao;

    @FXML
    private TextField nameInput;
    @FXML
    private TextField surnameInput;
    @FXML
    private TextField indexNumberInput;
    @FXML
    private TextField emailInput;

    @FXML
    private void initialize() {
        super.initialize("Participant");
        setParticipant(new Participant());
        ratingDao = new RatingDao();
    }

    public Optional<String> validateInput() {
        final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        String name = Optional.ofNullable(nameInput.getText()).orElse("").trim();
        String surname = Optional.ofNullable(surnameInput.getText()).orElse("").trim();
        String age = Optional.ofNullable(indexNumberInput.getText()).orElse("").trim();
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

    public void update() {
        participant.setName(nameInput.getText().trim());
        participant.setSurname(surnameInput.getText().trim());
        participant.setIndexNumber(indexNumberInput.getText());
        participant.setEmail(emailInput.getText());
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
        if (participant == null) {
            nameInput.clear();
            surnameInput.clear();
            indexNumberInput.clear();
            emailInput.clear();
        } else {
            nameInput.setText(participant.getName());
            surnameInput.setText(participant.getSurname());
            indexNumberInput.setText(participant.getIndexNumber());
            emailInput.setText(participant.getEmail());
        }
    }
}
