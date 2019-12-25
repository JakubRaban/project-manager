package pl.edu.agh.gastronomiastosowana.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Window;
import pl.edu.agh.gastronomiastosowana.model.interactions.ItemInputType;

public abstract class AbstractPresenter {

    private boolean accepted;
    private ItemInputType itemInputType;
    @FXML
    private Label dialogTypeLabel;
    private Window window;
    private String presenterType;

    @FXML
    public void initialize(String presenterType) {
        this.presenterType = presenterType;
        setItemInputType(ItemInputType.NEW_ITEM);
    }

    @FXML
    public void accept() {
        accepted = true;
        window.hide();
    }

    @FXML
    public void reject() {
        accepted = false;
        window.hide();
    }

    public void setWindow(Window window) {
        this.window = window;
        window.setOnCloseRequest(event -> reject());
    }

    public void setItemInputType(ItemInputType itemInputType) {
        this.itemInputType = itemInputType;
        String windowTitle = presenterType;
        if(this.itemInputType == ItemInputType.NEW_ITEM) {
            windowTitle = "Add new " + windowTitle;
        } else {
            windowTitle = "Edit " + windowTitle;
        }
        dialogTypeLabel.setText(windowTitle);
    }

    public boolean isAccepted() {
        return accepted;
    }

    public ItemInputType getItemInputType() {
        return itemInputType;
    }

}
