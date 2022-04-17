module DictionaryClient {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    // open any FXML controller class' packages to at least javafx.fxml
    opens edu.javafx to javafx.fxml;

    // export Application subclass's package
    exports edu;
}