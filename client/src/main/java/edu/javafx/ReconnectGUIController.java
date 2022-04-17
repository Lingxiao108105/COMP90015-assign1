package edu.javafx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ReconnectGUIController {

    @FXML
    private Button exitButton;

    @FXML
    private TextField port;

    @FXML
    private TextField ip;

    @FXML
    private Button connectButton;

    @FXML
    void connect(ActionEvent event) {

    }

    @FXML
    void exit(ActionEvent event) {
        Platform.exit();
    }

}

