package pl.edu.agh.gastronomiastosowana.model.aggregations;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import pl.edu.agh.gastronomiastosowana.model.Participant;

public class ParticipantList {
    private ListProperty<Participant> participants;

    public ParticipantList() {
        participants = new SimpleListProperty<>();
    }

    public ObservableList<Participant> getParticipants() {
        return participants.get();
    }

    public ListProperty<Participant> participantsProperty() {
        return participants;
    }

    public void setParticipants(ObservableList<Participant> participants) {
        this.participants.set(participants);
    }
}
