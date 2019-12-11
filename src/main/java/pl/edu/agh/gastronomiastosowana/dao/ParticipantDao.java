package pl.edu.agh.gastronomiastosowana.dao;

import org.hibernate.Session;
import pl.edu.agh.gastronomiastosowana.model.Participant;
import pl.edu.agh.gastronomiastosowana.session.SessionService;

import javax.persistence.Query;
import java.util.Optional;

public class ParticipantDao extends GenericDao<Participant> {

    public ParticipantDao() {
        super();
    }

    public Optional<Participant> findParticipantByEmail(String email){
        final Session session = SessionService.getSession();

        Optional result;

        Query participantsByEmailQuery = session.createQuery("from Participant as participant " +
                "where participant.email = :email", Participant.class);
        participantsByEmailQuery.setParameter("email", email);

        result = participantsByEmailQuery.getResultStream().findFirst();

        return result;
    }

}
