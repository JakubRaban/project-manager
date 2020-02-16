package pl.edu.agh.gastronomiastosowana.controller;

import javafx.beans.binding.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import pl.edu.agh.gastronomiastosowana.dao.ParticipantDao;
import pl.edu.agh.gastronomiastosowana.dao.ProjectGroupDao;
import pl.edu.agh.gastronomiastosowana.dao.RatingDao;
import pl.edu.agh.gastronomiastosowana.model.ProjectGroup;
import pl.edu.agh.gastronomiastosowana.model.aggregations.ParticipantList;
import pl.edu.agh.gastronomiastosowana.model.aggregations.ParticipantProjectGroupList;
import pl.edu.agh.gastronomiastosowana.model.aggregations.ProjectGroupList;
import pl.edu.agh.gastronomiastosowana.model.aggregations.model_wrapper.ParticipantProjectGroup;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class LeaderboardViewController {
    private ParticipantProjectGroupList participantList;
    private ParticipantDao participantDao;

    private ProjectGroupList projectGroupList;
    private ProjectGroupDao projectGroupDao;

    private Optional<ProjectGroup> selectedProjectGroup;

    private SortKey sortKey = SortKey.NONE;

    //Chyba do wywalenia:
    private RatingDao ratingDao;

    @FXML private TableView<ParticipantProjectGroup> participantTableView;
    @FXML private TableView<ProjectGroup> projectGroupTableView;

    @FXML private Button refreshButton;
    @FXML private Button showAllButton;
    @FXML private Button selectedProjectGroupOnlyButton;

    @FXML private Button sortByAverageButton;
    @FXML private Button sortByRatingCountButton;
    @FXML private Button sortByRatingSumButton;

    @FXML private Label indexLabel;
    @FXML private Label emailLabel;
    @FXML private Label registrationDateLabel;


    @FXML
    private void initialize(){
        participantList = new ParticipantProjectGroupList();
        participantDao = new ParticipantDao();

        projectGroupList = new ProjectGroupList();
        projectGroupDao = new ProjectGroupDao();

        ratingDao = new RatingDao();

        selectedProjectGroup = Optional.empty();


        bindTableProperties();
        bindButtonProperties();
        bindProjectGroupProperties();

        loadParticipants();
        loadProjectGroups();
    }

    private void bindTableProperties(){
        participantTableView.itemsProperty().bind(participantList.getProperty());
        projectGroupTableView.itemsProperty().bind(projectGroupList.getProperty());
    }

    private void bindButtonProperties(){
        BooleanBinding disableBinding = projectGroupTableView.getSelectionModel().selectedItemProperty().isNull();
        selectedProjectGroupOnlyButton.disableProperty().bind(disableBinding);
        selectedProjectGroupOnlyButton.disableProperty().bind(disableBinding);
    }

    private void bindProjectGroupProperties(){
        ObjectBinding<String> indexBinding = Bindings.select(participantTableView.getSelectionModel().selectedItemProperty(), "indexNumber");
                StringBinding indexStringBinding = Bindings
                .when(indexBinding.isNotNull())
                .then(indexBinding.asString())
                .otherwise("NO RATINGS");
        indexLabel.textProperty().bind(indexStringBinding);

        ObjectBinding<String> emailBinding = Bindings.select(participantTableView.getSelectionModel().selectedItemProperty(), "email");
        StringBinding emailStringBinding = Bindings
                .when(emailBinding.isNotNull())
                .then(emailBinding.asString())
                .otherwise("NO RATINGS");
        emailLabel.textProperty().bind(emailStringBinding);

        ObjectBinding<LocalDate> registrationDateBinding = Bindings.select(participantTableView.getSelectionModel().selectedItemProperty(), "registrationDate");
        StringBinding registrationDateStringBinding = Bindings
                .when(registrationDateBinding.isNotNull())
                .then(registrationDateBinding.asString())
                .otherwise("NO RATINGS");
        registrationDateLabel.textProperty().bind(registrationDateStringBinding);


    }


    @FXML
    private void loadParticipants(){

        ParticipantList participantList = new ParticipantList();

        if (selectedProjectGroup.isEmpty()) {
            participantList.setElements(FXCollections.observableList(participantDao.findAll()));
        }
        else{
            participantList.setElements(FXCollections.observableList(participantDao.findParticipantsAssignedTo(selectedProjectGroup.get())));
        }

        this.participantList.setWithParticipants(participantList);
        this.participantList.setSelectedProjectGroup(selectedProjectGroup);

        this.participantList.setElements( getSortedData() );
    }

    @FXML
    private void loadProjectGroups(){
        projectGroupList.setElements(FXCollections.observableList(projectGroupDao.findAll()));
    }

    @FXML
    public void showAllParticipants(ActionEvent actionEvent) {
        selectedProjectGroup = Optional.empty();

        loadParticipants();
    }

    public void showSelectedProjectGroupParticipants(ActionEvent actionEvent) {
        selectedProjectGroup = Optional.of(projectGroupTableView.getSelectionModel().getSelectedItem());

        loadParticipants();
    }


    public void sortByAverageRating(ActionEvent actionEvent) {
        sortKey = SortKey.AVERAGE;
        this.participantList.setElements( getSortedData() );
    }

    public void sortByRatingCount(ActionEvent actionEvent) {
        sortKey = SortKey.COUNT;
        this.participantList.setElements( getSortedData() );
    }

    public void sortByRatingSum(ActionEvent actionEvent) {
        sortKey = SortKey.SUM;
        this.participantList.setElements( getSortedData() );
    }


    private ObservableList<ParticipantProjectGroup> getSortedData(){
        Stream<ParticipantProjectGroup> stream = this.participantList.getElements().stream();

        switch(sortKey){
            case AVERAGE:
                stream = stream.sorted(Comparator.comparingDouble(ParticipantProjectGroup::getAverageRating).reversed());
                break;
            case COUNT:
                stream = stream.sorted(Comparator.comparing(ParticipantProjectGroup::getRatingCount).reversed());
                break;
            case SUM:
                stream = stream.sorted(Comparator.comparingDouble(ParticipantProjectGroup::getRatingSum).reversed());
                break;
        }

        return FXCollections.observableList(stream.collect(toList()));
    }

    public void refresh(ActionEvent actionEvent) {
        loadParticipants();
        loadProjectGroups();
    }
}

enum SortKey {
    NONE,
    AVERAGE,
    COUNT,
    SUM
}
