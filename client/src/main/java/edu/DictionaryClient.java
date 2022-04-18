package edu;

import edu.javafx.SearchGUIController;
import edu.server.Client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DictionaryClient extends Application {

    public static Client client = null;

    public static void main(String[] args){
        Platform.runLater(()->{
            if(args.length == 2){
                try {
                    DictionaryClient.client = new Client(args[0],Integer.parseInt(args[1]));
                } catch (Exception e){
                    System.out.println("Invalid address or port!");
                    DictionaryClient.client = new Client();
                }
            }else {
                DictionaryClient.client = new Client();
            }
            DictionaryClient.client.start();
        });
        launch();
    }

    @Override
    public void start(Stage stage) {

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
