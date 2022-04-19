package edu;

import edu.javafx.SearchGUIController;
import edu.server.Client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class DictionaryClient extends Application {

    public static Client client = null;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        //start the client
        Parameters parameters = getParameters();
        List<String> args = parameters.getRaw();
        if(args.size() >= 2){
            try {
                DictionaryClient.client = new Client(args.get(0),Integer.parseInt(args.get(1)));
            } catch (Exception e){
                System.out.println("Invalid address or port!");
                DictionaryClient.client = new Client();
            }
        }else {
            DictionaryClient.client = new Client();
        }
        DictionaryClient.client.start();


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
