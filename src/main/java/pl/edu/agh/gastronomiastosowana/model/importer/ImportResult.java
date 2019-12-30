package pl.edu.agh.gastronomiastosowana.model.importer;

import java.util.List;

public class ImportResult<T> {

    private List<T> importedEntities;
    private int successfullyImported;
    private int notImported;

    public List<T> getImportedEntities() {
        return importedEntities;
    }

    public void setImportedEntities(List<T> importedEntities) {
        this.importedEntities = importedEntities;
    }

    public int getSuccessfullyImported() {
        return successfullyImported;
    }

    public void setSuccessfullyImported(int successfullyImported) {
        this.successfullyImported = successfullyImported;
    }

    public int getNotImported() {
        return notImported;
    }

    public void setNotImported(int notImported) {
        this.notImported = notImported;
    }
}
