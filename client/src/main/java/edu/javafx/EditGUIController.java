package edu.javafx;

import edu.DictionaryClient;
import edu.server.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static edu.javafx.StatusConstant.*;
import static edu.javafx.StatusConstant.WARNING;

public class EditGUIController {

    @FXML
    private Button searchButton;

    @FXML
    private ListView<String> meaningView;

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
    private Label status;


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
    void toSearchMode(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(DictionaryClient.class.getResource("search.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 1095.0, 746.0);
        } catch (IOException e) {
            System.out.println("Fail to load the scene from edit.fxml");
        }
        if(scene != null){
            Stage stage = (Stage) backSearchModeButton.getScene().getWindow();
            stage.setScene(scene);
        }
    }


    @FXML
    void update(ActionEvent event) {

    }

    @FXML
    void add(ActionEvent event) {

    }

}

