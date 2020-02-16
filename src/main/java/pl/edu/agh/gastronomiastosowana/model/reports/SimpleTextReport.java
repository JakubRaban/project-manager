package pl.edu.agh.gastronomiastosowana.model.reports;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SimpleTextReport implements Report<String> {

    private String reportText;

    public SimpleTextReport(String reportText) {
        this.reportText = reportText;
    }

    @Override
    public String getReport() {
        return reportText;
    }

    @Override
    public String getAsString() {
        return getReport();
    }

    @Override
    public void saveToFile(Path pathToFile) throws IOException {
        Files.deleteIfExists(pathToFile);
        Files.writeString(pathToFile, getReport());
    }
}
