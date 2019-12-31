package pl.edu.agh.gastronomiastosowana.model.reports;

import java.io.IOException;
import java.nio.file.Path;

public interface Report<R> {

    R getReport();
    String getAsString();
    void saveToFile(Path pathToFile) throws IOException;

}
