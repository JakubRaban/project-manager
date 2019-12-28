package pl.edu.agh.gastronomiastosowana.presenter;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import pl.edu.agh.gastronomiastosowana.dao.RatingDao;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;
import pl.edu.agh.gastronomiastosowana.model.Rating;
import pl.edu.agh.gastronomiastosowana.model.aggregations.RatingList;

import java.util.Optional;

public class ProjectGroupRatingViewPanePresenter extends AbstractPresenter {

    private ProjectGroup projectGroup;
    private RatingList ratingList;
    private RatingDao ratingDao;

    @FXML private TableView<Rating> ratingsTableView;
    @FXML private Button editRatingButton;
    @FXML private Button removeRatingButton;

    public void initialize() {
        ratingList = new RatingList();
        ratingDao = new RatingDao();
        setProjectGroup(new ProjectGroup());
        bindTableProperties();
        bindButtonProperties();
        loadRatingList();
    }

    private void bindTableProperties() {
        ratingsTableView.itemsProperty().bind(ratingList.getProperty());
    }

    private void bindButtonProperties() {
        BooleanBinding disableBinding = ratingsTableView.getSelectionModel().selectedItemProperty().isNull();
        editRatingButton.disableProperty().bind(disableBinding);
        removeRatingButton.disableProperty().bind(disableBinding);
    }

    private void loadRatingList() {
        ratingList.setElements(FXCollections.observableList(ratingDao.findAll()));
    }

    public void setProjectGroup(ProjectGroup selectedGroup) {
        this.projectGroup = projectGroup;
    }

    @Override
    public Optional<String> validateInput() {
        return Optional.empty();
    }

    @Override
    public void update() {
        return;
    }
}
