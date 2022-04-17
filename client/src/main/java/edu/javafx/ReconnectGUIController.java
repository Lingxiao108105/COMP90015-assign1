package edu.javafx;

import edu.DictionaryClient;
import edu.server.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ReconnectGUIController implements Initializable {

    public static Stage stage = null;

    @FXML
    private Button exitButton;

    @FXML
    private TextField port;

    @FXML
    private TextField ip;

    @FXML
    private Button connectButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set ip and port
        Client client = DictionaryClient.client;
        ip.setText(client.getIp());
        port.setText(client.getPort().toString());
    }

    @FXML
    void connect(ActionEvent event) {
        Client client = DictionaryClient.client;
        client.interrupt();
        client = new Client(ip.getText(),Integer.parseInt(port.getText()));
        client.start();
        DictionaryClient.client = client;
        ReconnectGUIController.stage.close();
    }

    @FXML
    void exit(ActionEvent event) {
        Platform.exit();
    }

}

