package pl.edu.agh.gastronomiastosowana.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import pl.edu.agh.gastronomiastosowana.model.Project;
import pl.edu.agh.gastronomiastosowana.model.Task;

import java.time.LocalDate;
import java.util.Optional;


public class TaskEditPanePresenter extends AbstractPresenter{

    Task task;
    @FXML private TextField taskTitleInput;
    @FXML private TextField taskDetailsInput;
    @FXML private DatePicker deadlineInput;

    @FXML
    private void initialize() {
        super.initialize("Task");
        setTask(new Task());
    }

    @Override
    public Optional<String> validateInput(){
        String title = Optional.ofNullable(taskTitleInput.getText()).orElse("").trim();
        LocalDate deadline = deadlineInput.getValue();

        if (title.isEmpty())
            return Optional.of("Task title cannot be empty");
        if (deadline != null && deadline.compareTo(LocalDate.now()) < 0)
            return Optional.of("Deadline cannot be in the past!");
        return Optional.empty();
    }

    public void update() {
        task.setTitle(taskTitleInput.getText().trim());
        task.setDetails(taskDetailsInput.getText().trim());
        if (deadlineInput.getValue() != null) {
            task.setDeadline(deadlineInput.getValue());
        }

    }

    @FXML
    void clearDeadlineInput(ActionEvent event) {
        deadlineInput.setValue(null);
    }

    public void setTask(Task task){
        this.task = task;
        if(task == null){
            taskTitleInput.clear();
            taskDetailsInput.clear();
            deadlineInput.setValue(null);
        }
        else{
            taskTitleInput.setText(task.getTitle());
            taskDetailsInput.setText(task.getDetails());
            deadlineInput.setValue(task.getDeadline());
        }
    }

    public Task getTask(){
        return this.task;
    }
}
