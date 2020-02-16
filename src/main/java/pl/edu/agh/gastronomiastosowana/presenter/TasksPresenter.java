package pl.edu.agh.gastronomiastosowana.presenter;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import pl.edu.agh.gastronomiastosowana.dao.TaskDao;
import pl.edu.agh.gastronomiastosowana.model.Participant;
import pl.edu.agh.gastronomiastosowana.model.Project;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;
import pl.edu.agh.gastronomiastosowana.model.Task;
import pl.edu.agh.gastronomiastosowana.model.aggregations.TaskList;
import pl.edu.agh.gastronomiastosowana.model.interactions.ItemInputType;

import java.io.IOException;
import java.util.Optional;

public class TasksPresenter extends AbstractPresenter{

    private Project project;
    private TaskDao taskDao;
    private TaskList taskList;
    @FXML private Button addButton;
    @FXML private Button removeButton;
    @FXML private Button editButton;
    @FXML private TableView<Task> tableTasksView;
    @FXML private TextArea detailsTextArea;



    private void bindTableProperties(){
        tableTasksView.itemsProperty().bind(taskList.getProperty());
    }

    private void bindButtonProperties(){
        BooleanBinding disableBinding = tableTasksView.getSelectionModel().selectedItemProperty().isNull();

        editButton.disableProperty().bind(disableBinding);
        removeButton.disableProperty().bind(disableBinding);
    }

    private void bindLabelProperties(){
        ObjectBinding<String> detailsBinding = Bindings.select(tableTasksView.getSelectionModel().selectedItemProperty(), "details");
        detailsTextArea.textProperty().bind(detailsBinding);
        detailsTextArea.setWrapText(true);
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
        bindLabelProperties();
    }

    public void setProject(Project project){
        this.project = project;

        loadTasks();

        bindTableProperties();
    }

    public void addTask(){
        try{
            Dialog editDialog = new Dialog();
            FXMLLoader loader = new FXMLLoader();
            Parent parent = loader.load(getClass().getResourceAsStream("/fxml/TaskEditPane.fxml"));
            TaskEditPanePresenter presenter = loader.getController();
            presenter.setItemInputType(ItemInputType.NEW_ITEM);
            presenter.setWindow(editDialog.getDialogPane().getScene().getWindow());
            editDialog.getDialogPane().setContent(parent);
            editDialog.showAndWait();

            if (presenter.isAccepted()) {
                presenter.getTask().setAssessedProject(this.project);
                taskDao.save(presenter.getTask());
                taskList.getElements().add(presenter.getTask());
            }
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }


    public void removeTask(){
        Task selectedProject = tableTasksView.getSelectionModel().getSelectedItem();
        taskDao.delete(selectedProject);
        taskList.getElements().remove(selectedProject);
    }

    public void editTask(){
        Task selectedTask = tableTasksView.getSelectionModel().getSelectedItem();
        try{
            Dialog editDialog = new Dialog();
            FXMLLoader loader = new FXMLLoader();
            Parent parent = loader.load(getClass().getResourceAsStream("/fxml/TaskEditPane.fxml"));
            TaskEditPanePresenter presenter = loader.getController();
            presenter.setItemInputType(ItemInputType.EDIT_ITEM);
            presenter.setWindow(editDialog.getDialogPane().getScene().getWindow());
            presenter.setTask(selectedTask);
            editDialog.getDialogPane().setContent(parent);
            editDialog.showAndWait();

            if (presenter.isAccepted()) {
                presenter.getTask().setAssessedProject(this.project);
                taskDao.save(presenter.getTask());
            }
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
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
