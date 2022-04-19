package edu.javafx;

import edu.DictionaryClient;
import edu.client.*;
import edu.data.Meaning;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static edu.javafx.StatusConstant.*;

/**
 * controller for search scene
 *
 * @author lingxiao li 1031146
 */
public class SearchGUIController implements Initializable {

    //search scene
    private static Scene scene = null;

    @FXML
    private Button searchButton;

    @FXML
    private Button editButton;

    @FXML
    private Button removeButton;

    @FXML
    private TextField searchTestField;

    @FXML
    private TableView<Meaning> meaningTable;

    @FXML
    private TableColumn<Meaning, String> meaningColumn;

    @FXML
    private TableColumn<Meaning, String> POSColumn;

    @FXML
    private Label status;

    //singleton of search scene
    public static Scene getScene(){
        if(SearchGUIController.scene == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(DictionaryClient.class.getResource("search.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 1095.0, 746.0);
            } catch (IOException e) {
                System.out.println("Fail to load the scene from search.fxml");
            }
            SearchGUIController.scene = scene;
        }
        return SearchGUIController.scene;
    }

    /**
     * initialize the meaning table
     * @param url null
     * @param resourceBundle null
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        meaningTable.setPlaceholder(
                new Label("No meaning to display"));
        meaningTable.editableProperty().set(false);
        POSColumn.setCellValueFactory(new PropertyValueFactory<Meaning, String>("POS"));
        POSColumn.setCellFactory(TextAreaTableCell.forTableColumn(new DefaultStringConverter()));

        meaningColumn.setCellValueFactory(new PropertyValueFactory<Meaning, String>("meaning"));
        meaningColumn.setCellFactory(TextAreaTableCell.forTableColumn(new DefaultStringConverter()));
    }

    /**
     * query when press enter for search
     * @param event key event
     */
    @FXML
    void KeyPressedSearchTestField(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER) {
            query(null);
        }
    }

    /**
     * query the word when user press Search button
     * @param event null
     */
    @FXML
    void query(ActionEvent event) {

        //set status label
        status.setText(QUERY);
        status.setTextFill(WAITING);

        String spell = searchTestField.getText();
        //sanity check
        if(spell.isBlank() || spell.isEmpty()){
            status.setText(EMPTY_OR_BLANK_INPUT);
            status.setTextFill(WARNING);
            return;
        }

        //query
        Request request = RequestCreator.QueryRequest(spell);
        Client.enqueueRequest(request);

        //handle response
        Platform.runLater( () ->{
            handleQueryResponse(request);
        });

    }

    /**
     * handle the response of query
     * @param request request which need response
     */
    void handleQueryResponse(Request request) {
        Platform.runLater( () ->{
            Response response = Client.getResponse(request);
            //request is out-of-date
            if(!Client.latest(request)){
                return;
            }

            //Client did not receive response yet
            if(response == null){
                handleQueryResponse(request);
                return;
            }

            //set status
            status.setText(response.getStatus().toString());
            status.setTextFill(StatusConstant.colorByStatus(response.getStatus()));

            //show the meaning
            if(response.getWord().getMeanings() == null
                    || response.getWord().getMeanings().getMeaningMap() == null
                    || response.getWord().getMeanings().getMeaningMap().isEmpty()){
                //empty the table
                meaningTable.getItems().clear();
                return;
            }
            HashMap<String,String> meaningMap = response.getWord().getMeanings().getMeaningMap();
            List<Meaning> meaningList = Meaning.fromMap(meaningMap);
            ObservableList<Meaning> items = FXCollections.observableArrayList(meaningList);
            meaningTable.setItems(items);

        });

    }

    /**
     * remove the word when user press Remove button
     * @param event null
     */
    @FXML
    void remove(ActionEvent event) {

        //set status label
        status.setText(REMOVE);
        status.setTextFill(WAITING);

        String spell = searchTestField.getText();
        //sanity check
        if(spell.isBlank() || spell.isEmpty()){
            status.setText(EMPTY_OR_BLANK_INPUT);
            status.setTextFill(WARNING);
            return;
        }

        //remove
        Request request = RequestCreator.RemoveRequest(spell);
        Client.enqueueRequest(request);

        //handle response
        Platform.runLater( () ->{
            handleRemoveResponse(request);
        });

    }

    /**
     * handle the response of remove
     * @param request request which need response
     */
    void handleRemoveResponse(Request request) {
        Platform.runLater( () ->{
            Response response = Client.getResponse(request);
            //request is out-of-date
            if(!Client.latest(request)){
                return;
            }

            //Client did not receive response yet
            if(response == null){
                handleRemoveResponse(request);
                return;
            }

            //set status
            status.setText(response.getStatus().toString());
            status.setTextFill(StatusConstant.colorByStatus(response.getStatus()));

            //empty the table
            meaningTable.getItems().clear();

        });

    }

    /**
     * change the scene to edit scene
     * @param event null
     */
    @FXML
    void toEditMode(ActionEvent event) {
        Scene scene = EditGUIController.getScene();
        if(scene != null){
            Stage stage = (Stage) editButton.getScene().getWindow();
            stage.setScene(scene);
        }
    }

}
