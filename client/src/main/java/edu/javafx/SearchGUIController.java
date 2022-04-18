package edu.javafx;

import edu.DictionaryClient;
import edu.server.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static edu.javafx.StatusConstant.*;

public class SearchGUIController implements Initializable {

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
    private TableView<Map.Entry<String, String>> meaningTable;

    @FXML
    private TableColumn<Map.Entry<String, String>, String> meaningColumn;

    @FXML
    private TableColumn<Map.Entry<String, String>, String> POSColumn;

    @FXML
    private Label status;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        meaningTable.setPlaceholder(
                new Label("No meaning to display"));

        //adopt from https://stackoverflow.com/questions/18618653/binding-hashmap-with-tableview-javafx
        POSColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                // for first column we use key
                return new SimpleStringProperty(p.getValue().getKey());
            }
        });
        meaningColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
                // for second column we use value
                return new SimpleStringProperty(p.getValue().getValue());
            }
        });

        //adopt from https://stackoverflow.com/questions/22732013/javafx-tablecolumn-text-wrapping
        POSColumn.setCellFactory(param -> {
            return new TableCell<Map.Entry<String, String>, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        Text text = new Text(item);
                        text.setStyle("-fx-text-alignment:justify;");
                        text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(35));
                        setGraphic(text);
                    }
                }
            };
        });
        meaningColumn.setCellFactory(param -> {
            return new TableCell<Map.Entry<String, String>, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        Text text = new Text(item);
                        text.setStyle("-fx-text-alignment:justify;");
                        text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(35));
                        setGraphic(text);
                    }
                }
            };
        });
    }

    @FXML
    void KeyPressedSearchTestField(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER) {
            query(null);
        }
    }

    @FXML
    void query(ActionEvent event) {

        status.setText(RequestType.QUERY.toString());
        status.setTextFill(WAITING);

        String spell = searchTestField.getText();
        if(spell.isBlank() || spell.isEmpty()){
            status.setText(EMPTY_OR_BLANK_INPUT);
            status.setTextFill(WARNING);
            return;
        }

        Request request = RequestCreator.QueryRequest(spell);
        Client.enqueueRequest(request);
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
            //Client did not receive response yet
            if(response == null){
                handleQueryResponse(request);
                return;
            }
            //response is out-of-date
            if(!Client.latest(response)){
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
            ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(meaningMap.entrySet());
            meaningTable.setItems(items);

        });

    }


    @FXML
    void remove(ActionEvent event) {

        status.setText(RequestType.REMOVE.toString());
        status.setTextFill(WAITING);

        String spell = searchTestField.getText();
        if(spell.isBlank() || spell.isEmpty()){
            status.setText(EMPTY_OR_BLANK_INPUT);
            status.setTextFill(WARNING);
            return;
        }

        Request request = RequestCreator.RemoveRequest(spell);
        Client.enqueueRequest(request);
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
            //Client did not receive response yet
            if(response == null){
                handleQueryResponse(request);
                return;
            }
            //response is out-of-date
            if(!Client.latest(response)){
                return;
            }

            //set status
            status.setText(response.getStatus().toString());
            status.setTextFill(StatusConstant.colorByStatus(response.getStatus()));

            //empty the table
            meaningTable.getItems().clear();

        });

    }

    @FXML
    void toEditMode(ActionEvent event) {
        Scene scene = EditGUIController.getScene();
        if(scene != null){
            Stage stage = (Stage) editButton.getScene().getWindow();
            stage.setScene(scene);
        }
    }

}
