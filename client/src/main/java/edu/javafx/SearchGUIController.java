package edu.javafx;

import edu.DictionaryClient;
import edu.common.enums.Status;
import edu.server.*;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static edu.javafx.StatusConstant.*;

public class SearchGUIController implements Initializable {

    @FXML
    private Button searchButton;

    @FXML
    private ListView<String> meaningView;

    @FXML
    private Button editButton;

    @FXML
    private Button removeButton;

    @FXML
    private TextField searchTestField;

    @FXML
    private Label status;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //https://stackoverflow.com/questions/53493111/javafx-wrapping-text-in-listview
        //Wrapping text in ListView
        meaningView.setCellFactory(param -> new ListCell<String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item==null) {
                    setGraphic(null);
                    setText(null);
                }else{
                    // set the width's
                    setMinWidth(param.getWidth());
                    setMaxWidth(param.getWidth());
                    setPrefWidth(param.getWidth());
                    // allow wrapping
                    setWrapText(true);
                    setText(item.toString());
                }
            }
        });
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
            if(response.getWord().getMeanings() == null){
                meaningView.setItems(null);
                return;
            }
            searchTestField.setText(response.getWord().getSpell());
            ObservableList<String> meanings = FXCollections.observableArrayList(
                    response.getWord().getMeanings().toList());
            meaningView.setItems(meanings);


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



        });

    }

    @FXML
    void toEditMode(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(DictionaryClient.class.getResource("edit.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 1095.0, 746.0);
        } catch (IOException e) {
            System.out.println("Fail to load the scene from edit.fxml");
        }
        if(scene != null){
            Stage stage = (Stage) editButton.getScene().getWindow();
            stage.setScene(scene);
        }
    }

}
