package edu;

import edu.javafx.SearchGUIController;
import edu.server.Client;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DictionaryClient extends Application {

    public static Client client = null;

    public static void main(String[] args){
        launch();
    }


    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage stage) {

        client = new Client();
        client.start();

        Scene scene = SearchGUIController.getScene();
        stage.setTitle("Dictionary");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        client.interrupt();
    }
}
