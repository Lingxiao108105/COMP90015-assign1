package edu;

import edu.server.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class DictionaryClient extends Application {

    public static void main(String[] args){
        launch();
    }


    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage stage) {

        new Client().start();

        FXMLLoader fxmlLoader = new FXMLLoader(DictionaryClient.class.getResource("search.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 1095.0, 746.0);
        } catch (IOException e) {
            System.out.println("Fail to load the scene from search.fxml");
        }
        stage.setTitle("Dictionary");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
