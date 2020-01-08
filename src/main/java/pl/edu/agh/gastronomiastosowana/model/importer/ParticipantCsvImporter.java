package pl.edu.agh.gastronomiastosowana.model.importer;

import pl.edu.agh.gastronomiastosowana.model.Participant;

import java.nio.file.Path;

public class ParticipantCsvImporter extends AbstractCsvImporter<Participant> {
    public ParticipantCsvImporter(Path importedFile) {
        super(importedFile);
    }
}
