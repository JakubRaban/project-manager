package pl.edu.agh.gastronomiastosowana.model.lists;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import pl.edu.agh.gastronomiastosowana.model.Project;

import java.util.Collection;

public class ProjectList {
    private ListProperty<Project> projects;

    public ProjectList() {
        projects = new SimpleListProperty<>();
    }

    public ObservableList<Project> getProjects() {
        return projects.get();
    }

    public ListProperty<Project> projectsProperty() {
        return projects;
    }

    public void setProjects(ObservableList<Project> projects) {
        this.projects.set(projects);
    }
}
