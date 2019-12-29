package pl.edu.agh.gastronomiastosowana.model.reports;

public interface ReportGenerator<T> {

    Report<T> generate();

}
