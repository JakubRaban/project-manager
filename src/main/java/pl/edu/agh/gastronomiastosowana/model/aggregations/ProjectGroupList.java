package pl.edu.agh.gastronomiastosowana.model.aggregations;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;

public class ProjectGroupList {
    private ListProperty<ProjectGroup> projectGroups;

    public ProjectGroupList() {
        projectGroups = new SimpleListProperty<>();
    }

    public ObservableList<ProjectGroup> getProjectGroups() {
        return projectGroups.get();
    }

    public ListProperty<ProjectGroup> projectGroupProperty() {
        return projectGroups;
    }

    public void setProjectGroups(ObservableList<ProjectGroup> projectGroups) {
        this.projectGroups.set(projectGroups);
    }
}
