package edu;

import edu.javafx.SearchGUIController;
import edu.client.Client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.List;

/**
 * start point of javafx GUI
 * @author lingxiao li 1031146
 */
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

        //show search scene
        Scene scene = SearchGUIController.getScene();
        stage.setTitle("Dictionary");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Platform.exit();
            }
        });
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * stop the javafx GUI
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        client.interrupt();
    }
}
