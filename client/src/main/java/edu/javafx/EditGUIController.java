package edu.javafx;

import edu.DictionaryClient;
import edu.data.Meaning;
import edu.client.*;
import javafx.application.Platform;
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
import static edu.javafx.StatusConstant.WARNING;

/**
 * controller for edit scene
 *
 * @author lingxiao li 1031146
 */
public class EditGUIController implements Initializable {

    //edit scene
    private static Scene scene = null;

    @FXML
    private Button searchButton;

    @FXML
    private Button UpdateButton;

    @FXML
    private Button backSearchModeButton;

    @FXML
    private Button AddButton;

    @FXML
    private TextField searchTestField;

    @FXML
    private Button RemoveButton;

    @FXML
    private TableView<Meaning> meaningTable;

    @FXML
    private TableColumn<Meaning, String> meaningColumn;

    @FXML
    private TableColumn<Meaning, String> POSColumn;

    @FXML
    private Label status;

    @FXML
    private TextField POSAddTestField;

    @FXML
    private TextArea meaningAddTestArea;

    @FXML
    private Button addRowButton;

    @FXML
    private Button removeRowButton;

    //singleton of edit scene
    public static Scene getScene(){
        if(EditGUIController.scene == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(DictionaryClient.class.getResource("edit.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 1095.0, 746.0);
            } catch (IOException e) {
                System.out.println("Fail to load the scene from edit.fxml");
            }
            EditGUIController.scene = scene;
        }
        return EditGUIController.scene;
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
        meaningTable.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.DELETE){
                    removeRow(null);
                }
            }
        });
        POSColumn.setCellValueFactory(new PropertyValueFactory<Meaning, String>("POS"));
        POSColumn.setCellFactory(TextAreaTableCell.forTableColumn(new DefaultStringConverter()));
        POSColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Meaning, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Meaning, String> event) {
                Meaning value = event.getRowValue();
                String newValue = event.getNewValue();
                if(newValue.isBlank()){
                    removeRow(null);
                }else {
                    value.setPOS(newValue);
                }
            }
        });

        meaningColumn.setCellValueFactory(new PropertyValueFactory<Meaning, String>("meaning"));
        meaningColumn.setCellFactory(TextAreaTableCell.forTableColumn(new DefaultStringConverter()));
        meaningColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Meaning, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Meaning, String> event) {
                Meaning value = event.getRowValue();
                String newValue = event.getNewValue();
                if(newValue.isBlank()){
                    removeRow(null);
                }else {
                    value.setMeaning(newValue);
                }
            }
        });
    }

    /**
     * query the word when user press Search button
     * @param event null
     */
    @FXML
    void query(ActionEvent event) {

        //set status label
        status.setText(RequestType.QUERY.toString());
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
        status.setText(RequestType.REMOVE.toString());
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
     * update the word when user press update button
     * @param event null
     */
    @FXML
    void update(ActionEvent event) {

        //set status label
        status.setText(RequestType.UPDATE.toString());
        status.setTextFill(WAITING);

        String spell = searchTestField.getText();
        //sanity check
        if(spell.isBlank() || spell.isEmpty()){
            status.setText(EMPTY_OR_BLANK_INPUT);
            status.setTextFill(WARNING);
            return;
        }
        ObservableList<Meaning> meanings = meaningTable.getItems();
        if(meanings.isEmpty()){
            status.setText(EMPTY_MEANING_TABLE);
            status.setTextFill(WARNING);
            return;
        }

        //update
        Request request = RequestCreator.UpdateRequest(spell,Meaning.listToMeanings(meanings));
        Client.enqueueRequest(request);

        //handle response
        Platform.runLater( () ->{
            handleUpdateAndAddResponse(request);
        });
    }

    /**
     * handle the response of update and add
     * @param request request which need response
     */
    void handleUpdateAndAddResponse(Request request) {
        Platform.runLater( () ->{
            Response response = Client.getResponse(request);
            //request is out-of-date
            if(!Client.latest(request)){
                return;
            }

            //Client did not receive response yet
            if(response == null){
                handleUpdateAndAddResponse(request);
                return;
            }

            //set status
            status.setText(response.getStatus().toString());
            status.setTextFill(StatusConstant.colorByStatus(response.getStatus()));

        });
    }

    /**
     * add the word when user press add button
     * @param event null
     */
    @FXML
    void add(ActionEvent event) {

        //set status label
        status.setText(RequestType.ADD.toString());
        status.setTextFill(WAITING);

        //sanity check
        String spell = searchTestField.getText();
        if(spell.isBlank() || spell.isEmpty()){
            status.setText(EMPTY_OR_BLANK_INPUT);
            status.setTextFill(WARNING);
            return;
        }
        ObservableList<Meaning> meanings = meaningTable.getItems();
        if(meanings.isEmpty()){
            status.setText(EMPTY_MEANING_TABLE);
            status.setTextFill(WARNING);
            return;
        }

        //send request
        Request request = RequestCreator.AddRequest(spell,Meaning.listToMeanings(meanings));
        Client.enqueueRequest(request);

        //handle response
        Platform.runLater( () ->{
            handleUpdateAndAddResponse(request);
        });

    }

    /**
     * change the scene to search scene
     * @param event null
     */
    @FXML
    void toSearchMode(ActionEvent event) {
        Scene scene = SearchGUIController.getScene();
        if(scene != null){
            Stage stage = (Stage) backSearchModeButton.getScene().getWindow();
            stage.setScene(scene);
        }
    }

    /**
     * add a row to meaning table
     * @param event null
     */
    @FXML
    void addRow(ActionEvent event) {
        //sanity check
        if(POSAddTestField.getText().isBlank()){
            status.setText(BLANK_POS_ROW);
            status.setTextFill(WARNING);
            return;
        }
        if(meaningAddTestArea.getText().isBlank()){
            status.setText(BLANK_MEANING_ROW);
            status.setTextFill(WARNING);
            return;
        }

        ObservableList<Meaning> items = meaningTable.getItems();
        //check whether there are same POS
        for(Meaning meaning: items){
            if(meaning.getPOS().equals(POSAddTestField.getText())){
                status.setText(DUPLICATED_POS);
                status.setTextFill(WARNING);
                return;
            }
        }
        meaningTable.getItems().add(new Meaning(POSAddTestField.getText(),meaningAddTestArea.getText()));
    }

    /**
     * remove the selected row to meaning table
     * @param event null
     */
    @FXML
    void removeRow(ActionEvent event) {
        meaningTable.getItems().removeAll(
                meaningTable.getSelectionModel().getSelectedItems()
        );
    }

}

