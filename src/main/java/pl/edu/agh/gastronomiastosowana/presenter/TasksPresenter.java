package pl.edu.agh.gastronomiastosowana.presenter;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import pl.edu.agh.gastronomiastosowana.dao.TaskDao;
import pl.edu.agh.gastronomiastosowana.model.Participant;
import pl.edu.agh.gastronomiastosowana.model.Project;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;
import pl.edu.agh.gastronomiastosowana.model.Task;
import pl.edu.agh.gastronomiastosowana.model.aggregations.TaskList;

import java.util.Optional;

public class TasksPresenter extends AbstractPresenter{

    private Project project;
    private TaskDao taskDao;
    private TaskList taskList;
    @FXML private Button addButton;
    @FXML private Button removeButton;
    @FXML private Button editButton;
    @FXML private TableView<Task> tableTasksView;



    private void bindTableProperties(){
        tableTasksView.itemsProperty().bind(taskList.getProperty());
    }

    private void bindButtonProperties(){
        BooleanBinding disableBinding = tableTasksView.getSelectionModel().selectedItemProperty().isNull();

        addButton.disableProperty().bind(disableBinding);
        editButton.disableProperty().bind(disableBinding);
        removeButton.disableProperty().bind(disableBinding);
    }

    private void loadTasks(){
        taskList.setElements(FXCollections.observableList(
                taskDao.findRatingsByProjectName(project.getName())));
    }

    @FXML
    public void initialize() {
        super.initialize("Tasks in Project");

        taskDao = new TaskDao();
        taskList = new TaskList();

        bindButtonProperties();
    }

    public void setProject(Project project){
        this.project = project;

        loadTasks();

        bindTableProperties();
    }

    public void addTask(){

    }

    public void removeTask(){

    }

    public void editTask(){

    }

    @Override
    public void update() {
        return;
    }

    @Override
    public Optional<String> validateInput() {
        return Optional.empty();
    }
}
